from home.PTA_Modules import *
from home.models import Tank_data,Tanks,Container
from home.compute_w_level import calculate_level,compute_life_time
from datetime import datetime

ON_LEVEL = 75 # level in percent
OFF_LEVEL = 100 # level in percent

SUBTRACT_HEIGHT = 50 # height in mm , this is to save the sensor from water
DEFAULT_HEIGHT = 300# height in mm
DEFAULT_VOLUME =10# volume in litre
DEFAULT_STATUS = "Static"
DEFAULT_VALVE = 0
DEFAULT_LEVEL = 50
DEFAULT_LEVEL1 = 49

ID_STRING ="Id"
APP_STRING= "App"
PUMP_STRING = "Pump"
TANK_STRING = "Tank"
NEW_STRING = "New"
EDIT_STRING = "Edit"
IND_ID_STRING = "Ind_Id"
UPDATE_STRING = "Update"
W_LEVEL_STRING = "W_dist"
W_STATUS_STRING = "W_Status"
NAME_STRING = "Name"
LIFE_TIME_STRING = "Life_time"
STATUS_STRING = "Status"
VOLUME_STRING = "Vol"
HEIGHT_STRING = "Height"
VALVE_STRING ="Valve"
WATER_LEVEL = "W_Level"
EMPTY_STRING = "Empty"

class PTA_Container:
    temp_pump = pump()
    main_dict = {APP_STRING:[],
                PUMP_STRING:[temp_pump],# always new pump object placed at 0 position.
                TANK_STRING:{}}
    # pump_avial = 0 # 0 means pump connected or 1 means pump not connected
                                        

    # def check_container(self,_cont_id):
    #     temp_cont = self.main_dict.get(_cont_id)
    #     if temp_cont:
    #         # print("Container already exist")
    #         return True
    #     return False

    def add_app(self,_socket):
        temp_app = App()
        temp_app.set_app_web_socket(_socket)
        try:
            self.main_dict[APP_STRING].append(temp_app)
        except Exception as ex:
            print(ex)
    
    def add_tank(self,_ind_id,_water_dist):
        self.main_dict[TANK_STRING][_ind_id]="tank"+str(_ind_id)
        self.add_tank_in_db(_ind_id)# adding tank obj in db only once
        
    def set_the_w_level_flactuation(self,tank_id,_level):
        tank_data_ = Tank_data.objects.filter(Tank_id = tank_id).last()
        if self.main_dict.get(PUMP_STRING)[0].get_pump_status()==0:
            if tank_data_.water_level>_level:
                return True
        elif self.main_dict.get(PUMP_STRING)[0].get_pump_status()==1:
            if tank_data_.water_level<_level:
                return True
        return False
    def update_tank_water_level(self,_ind_id,_water_dist):
        tank_obj = Tanks.objects.get(Id = _ind_id)
        _level = calculate_level(tank_obj.height,_water_dist)
        if self.set_the_w_level_flactuation(_ind_id,_level):
            if _level > OFF_LEVEL :
                _level = OFF_LEVEL

            if self.main_dict[PUMP_STRING]:
                pump_mode =self.main_dict[PUMP_STRING][0].get_pump_status()
                self.add_tankdata_in_db(_ind_id,_level,pump_mode)
            else:
                self.add_tankdata_in_db(_ind_id,_level,0) # 0 means pump off
            # self.send_water_level_to_apps(_cont_id,_ind_id,_level)
            self.automate_pump(_ind_id,_level)
            self.send_data_to_apps()

    def automate_pump(self,_ind_id,_level):
        tank_obj = Tanks.objects.get(Id = _ind_id)
        if _level<=ON_LEVEL and tank_obj.valve_no != DEFAULT_VALVE:
           self.on_off_to_pump(_ind_id,1)
        elif _level>=OFF_LEVEL:
            self.on_off_to_pump(_ind_id,0)

    def send_data_to_apps(self):
        if self.main_dict[APP_STRING]:
            data  = self.gather_tanks_data()
            for app in self.main_dict[APP_STRING]:
                    app.get_app_web_socket().send(str(data))

        
                
    def send_water_level_to_apps(self,_cont_id,_tank_id,_level):
        if self.main_dict.get(_cont_id)[APP_STRING]:
            temp_dic = {WATER_LEVEL:_level,IND_ID_STRING:_tank_id}
            for app in self.main_dict.get(_cont_id)[APP_STRING]:
                app.get_app_web_socket().send(str(temp_dic))



    def add_pump(self,water_status,web_socket):
        # self.pump_avial =1
        temp_pump = self.main_dict[PUMP_STRING][0]
        temp_pump.set_water_status(water_status)
        temp_pump.set_web_socket(web_socket)
        temp_pump.set_avail_status(1)
        


    def remove_app(self,_socket):
        for tank in self.main_dict[APP_STRING]:
            if tank.get_app_web_socket() ==_socket:
                self.main_dict[APP_STRING].remove(tank)

    def remove_tank(self,_ind_id):
        flag = self.main_dict[TANK_STRING].get(_ind_id)
        if flag:
            self.main_dict[TANK_STRING].pop(_ind_id)

    def remove_pump(self,_websocket):
        # self.pump_avial =0
        temp_pump=self.main_dict[PUMP_STRING][0]
        temp_pump.set_avail_status(0)

    def send(self):
        pass
    
    def display(self):
        print(self.main_dict)

    def add_container_in_db(self,_id):
        if not self.check_container_in_db(_id):
            cont_name = "Container" + str(_id)
            temp_cont = Container(Id = _id,Name = cont_name)
            temp_cont.save()
        
    def remove_container_in_db(self,_id):
        pass

    def add_default_data_in_db(self,tank_instance):
        date_time = datetime.now()
        default = Tank_data(Tank_id=tank_instance,Date_time=date_time,pump_mode=0,water_level=DEFAULT_LEVEL)
        default.save()

        date_time = datetime.now()
        default = Tank_data(Tank_id=tank_instance,Date_time=date_time,pump_mode=0,water_level=DEFAULT_LEVEL1)
        default.save()


    def add_tank_in_db(self,_tank_id):
        if not self.check_tank_in_db(_tank_id):
            tank_name = "Tank" + str(_tank_id)
            temp_tank = Tanks(Id = _tank_id,Name = tank_name,height =DEFAULT_HEIGHT,volume =DEFAULT_VOLUME,valve_no=DEFAULT_VALVE )
            temp_tank.save()
            self.add_default_data_in_db(temp_tank)
    
    def remove_tank_fr_db(self,_tank_id):
        pass
    
    def add_tankdata_in_db(self,_tank_id,water_level,_pump_mode):
        date_time = datetime.now()
        temp_tank = Tanks.objects.get(Id = _tank_id)
        # below function give level in percent
        temp_tankdata = Tank_data(Tank_id=temp_tank,Date_time=date_time,
                                      water_level = water_level,pump_mode=_pump_mode)
        temp_tankdata.save()
    
    
    def check_container_in_db(self,_id):
        temp_cont  = Container.objects.filter(Id=_id)
        if temp_cont:
            return True
        return False
    
    
    def check_tank_in_db(self,tank_id):
        temp_tank = Tanks.objects.filter(Id = tank_id)
        if temp_tank:
            return True
        return False
    
    
    def check_tank_data_in_db(self,_tank_id):
        pass

    def gather_tanks_data(self):
        tanks = Tanks.objects.all()
        parent_dic = dict()
        if tanks:
            for tank in tanks:
                child_dic = dict()
                child_dic[ID_STRING] = tank.Id
                child_dic[NAME_STRING]= tank.Name
                child_dic[LIFE_TIME_STRING]= compute_life_time(tank_id=tank.Id)
                child_dic[STATUS_STRING]= tank.status
                child_dic[VOLUME_STRING]=tank.volume
                child_dic[VALVE_STRING]=tank.valve_no
                first_obj=Tank_data.objects.filter(Tank_id=tank.Id).last()
                if first_obj:
                    child_dic[WATER_LEVEL]=first_obj.water_level
                else:
                    child_dic[WATER_LEVEL]=DEFAULT_LEVEL
                if self.main_dict[PUMP_STRING]:
                    child_dic[W_STATUS_STRING]=self.main_dict[PUMP_STRING][0].get_water_status()
                    child_dic[PUMP_STRING]=self.main_dict[PUMP_STRING][0].get_pump_status()
                parent_dic[str(tank.Id)] = child_dic
            # print(parent_dic)
        else:
            parent_dic[EMPTY_STRING]=1
        return parent_dic
    
    def edit_tank_in_db(self,tanK_Id,_name,_height,_volume,_valve):
        temp_tank = Tanks.objects.get(Id = tanK_Id)
        temp_tank.Name = _name
        temp_tank.height = _height 
        temp_tank.volume = _volume
        temp_tank.valve_no= _valve
        temp_tank.save()

    def update_line_water_status(self,_status):
        self.main_dict[PUMP_STRING][0].set_water_status(_status)
    

    def on_off_to_pump(self,tank_id,status):
        pump_obj = self.main_dict[PUMP_STRING][0]
        if pump_obj.is_pump_avail():
            tank_obj = Tanks.objects.get(Id=tank_id)
            if status:# means status ==1
                if not pump_obj.get_pump_status():
                    tank_data = Tank_data.objects.filter(Tank_id=tank_id).last() # it gives the last updated value
                    if tank_data.water_level<OFF_LEVEL:
                        temp_tup = (1,tank_obj.valve_no)
                        pump_obj.get_web_socket().send(str(temp_tup))
                        print(temp_tup)
                        pump_obj.set_pump_status(1)
                        tank_obj.status= 1 # it means water is filling in tanky
                        tank_obj.save()
            elif status==0:
                if pump_obj.get_pump_status():
                    temp_tup = (0,0)
                    pump_obj.get_web_socket().send(str(temp_tup))
                    pump_obj.set_pump_status(0)
                    tanks = Tanks.objects.all()
                    for tank_obj in tanks:
                        tank_obj.status= 0 # # it means water is not filling in tanky
                        tank_obj.save()