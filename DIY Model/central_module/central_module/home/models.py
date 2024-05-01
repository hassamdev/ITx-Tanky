from django.db import models
# Create your models here.

class Container(models.Model):
    Id = models.IntegerField(primary_key=True,verbose_name="Id")
    Name = models.TextField(null=False)


class Tanks(models.Model):
    Id = models.IntegerField(primary_key=True)
    Name = models.TextField(null=False)
    height = models.IntegerField(null = True)
    volume = models.IntegerField(null = True)
    life_time = models.TextField(null=True)
    status = models.SmallIntegerField(default = 0) # its value either static or water filling
    valve_no = models.SmallIntegerField(default =0)

class Tank_data(models.Model):
    Id = models.IntegerField(primary_key=True,auto_created=True)
    Tank_id = models.ForeignKey(Tanks,on_delete=models.CASCADE)
    Date_time = models.DateTimeField(null=False)
    water_level = models.IntegerField(null=False)
    pump_mode = models.IntegerField(null= False)
