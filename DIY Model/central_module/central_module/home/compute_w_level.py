from datetime import datetime,timedelta
# from home.models import Tank_data
from django.db import connection


def calculate_level(actual_height:int,water_dist:int):
    water_height = actual_height - water_dist

    return (water_height/actual_height)*100





def compute_life_time(tank_id):
    curr_lev = curr_time = last_lev = last_time =0

    query = f"SELECT distinct water_level,strftime('%s',date_time) FROM home_tank_data where pump_mode =0 and tank_id_id ='{tank_id}' ORDER BY id DESC LIMIT 2 "
    
    with connection.cursor() as cursor:
        cursor.execute(query)
        # Fetch the results
        results = cursor.fetchall()
        # print(results)
        if results:
            curr_lev = results[0][0]
            curr_time = int(results[0][1])
            last_lev = results[1][0]
            last_time = int(results[1][1])
    
    # return compute(43,2,500)

    level = last_lev-curr_lev
    time = last_time-curr_time
    # time =500
    if level<=0 or time <=0:
        return None
    elif level ==None or time ==None:
        return None
    else:
        return compute(curr_lev,lev_dif=level,time_diff=time)


    
def compute(cur_lev,lev_dif,time_diff):
    one_percent_takes = time_diff/lev_dif
    # in 1 hour = 3600

    return str(round(((one_percent_takes * cur_lev)/3600),2)) + " hr"
