from channels.generic.websocket import WebsocketConsumer
from home.PTA_Container import *
import json

CONTAINER = PTA_Container()

class AppConsumer(WebsocketConsumer):
    cont_id = None
    def connect(self):
        self.accept()

    def receive(self, text_data=None):
        print("app:",text_data)
        temp_dict = json.loads(text_data)
        cont_id = temp_dict.get(ID_STRING)
        new_key = temp_dict.get(NEW_STRING)  # for addinng app in main dict initially
        pump_key = temp_dict.get(PUMP_STRING) # for sending command to pump
        edit_key = temp_dict.get(EDIT_STRING) # for editing tanky details.

        if cont_id:
            if new_key:
                # self.cont_id = cont_id
                CONTAINER.add_app(self)
                data= CONTAINER.gather_tanks_data()
                self.send(str(data))
            elif pump_key:
                # generate command to pump module
                tank_id = temp_dict.get(IND_ID_STRING)
                status = temp_dict.get(STATUS_STRING)# on /off status
                CONTAINER.on_off_to_pump(tank_id=tank_id,status = status)
                CONTAINER.send_data_to_apps()
            elif edit_key:
                # do something for edit the tanks detail
                tank_id = temp_dict.get(IND_ID_STRING)
                name = temp_dict.get(NAME_STRING)
                volume = temp_dict.get(VOLUME_STRING)
                height = temp_dict.get(HEIGHT_STRING)
                valve = temp_dict.get(VALVE_STRING)
                CONTAINER.edit_tank_in_db(tank_id,name,height,volume,valve)
                CONTAINER.send_data_to_apps()
            else:
                print("disconnect1")
                self.disconnect(125)
            # print(temp_dict.get('id'))
        else:
            print("disconnect2")
            self.disconnect(125)
        CONTAINER.display()
            
        

    def disconnect(self, close_code):
        # Called when the socket closes
        CONTAINER.remove_app(self)
        self.close()


class TankConsumer(WebsocketConsumer):
    cont_id = None
    ind_id = None
    def connect(self):
        self.accept()

    def receive(self, text_data=None):
        print(text_data)
        if text_data.find(",") != -1:
            tank_Id,w_dist = text_data.split(",")
            # print(tank_Id,w_dist)        

            if not CONTAINER.main_dict.get(TANK_STRING).get(tank_Id):
                CONTAINER.add_tank(tank_Id,w_dist)
                self.ind_id = tank_Id
                CONTAINER.display() # printing updated container
            CONTAINER.update_tank_water_level(tank_Id,int(w_dist)-SUBTRACT_HEIGHT)

    def disconnect(self, close_code):
        # Called when the socket closes
        if self.ind_id:
            CONTAINER.remove_tank(self.ind_id)
        self.close()







class PumpConsumer(WebsocketConsumer):
    def connect(self):
        # Called on connection.
        # To accept the connection call:
        self.accept()
        


    def receive(self, text_data=None):
        print("pump:",text_data)
        temp_dict = json.loads(text_data)
        cont_id = temp_dict.get(ID_STRING)
        new_key = temp_dict.get(NEW_STRING)
        update_key = temp_dict.get(UPDATE_STRING)
        w_status = temp_dict.get(W_STATUS_STRING)

        if cont_id:        
            if new_key:
                CONTAINER.add_pump(w_status,self)
            elif update_key:
                # generate command to pump module
                CONTAINER.update_line_water_status(w_status)
                CONTAINER.send_data_to_apps()
            else:
                self.disconnect(125)
            # print(temp_dict.get('id'))
        else:
            self.disconnect(125)
        CONTAINER.display()
            
        

    def disconnect(self, close_code):
        # Called when the socket closes
        CONTAINER.remove_pump(self)
        self.close()
        