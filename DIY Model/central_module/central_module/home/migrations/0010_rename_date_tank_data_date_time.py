# Generated by Django 4.2.6 on 2023-12-28 07:26

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('home', '0009_remove_tank_data_time_alter_tank_data_date'),
    ]

    operations = [
        migrations.RenameField(
            model_name='tank_data',
            old_name='Date',
            new_name='Date_time',
        ),
    ]
