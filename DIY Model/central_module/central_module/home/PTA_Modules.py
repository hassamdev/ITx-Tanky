

class pump:
    id = None
    avail_status = 0
    water_status = 0 # available =1 ,not available =0
    pump_status = 0 # ON =1 or Off =0 STATUS
    pump_web_socket = None

    def set_avail_status(self,status):
        self.avail_status = status

    def set_web_socket(self,_socket):
        self.pump_web_socket = _socket
    
    def set_pump_status(self,_status):
        self.pump_status= _status

    def set_water_status(self,_status):
        self.water_status = _status
    
    def set_id(self,_id):
        self.id = _id

    def get_id(self):
        return self.id
    
    def is_pump_avail(self):
        return self.avail_status
    def get_pump_status(self):
        return self.pump_status

    def get_water_status(self):
        return self.water_status
    
    def get_web_socket(self):
        return self.pump_web_socket

class tank:
    id = None
    individual_id = None
    water_level = None # distance between water surface and sensor in cm

    def set_water_level(self,_level):
        self.water_level = _level
    
    def set_id(self,_id):
        self.id = _id
    
    def set_ind_id(self,_ind_id):
        self.individual_id = _ind_id

    def get_ind_id(self):
        return self.individual_id
    
    def get_id(self):
        return self.id

    def get_water_status(self):
        return self.water_level


class App:
    id = None
    app_web_socket = None
    
    def set_app_web_socket(self,_web_socket):
        self.app_web_socket = _web_socket

    def set_id(self,_id):
        self.id = _id

    def get_id(self):
        return self.id

    def get_app_web_socket(self):
        return self.app_web_socket
