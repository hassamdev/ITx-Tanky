# Generated by Django 4.2.6 on 2023-12-14 09:56

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('home', '0002_alter_tank_data_id'),
    ]

    operations = [
        migrations.AlterField(
            model_name='tanks',
            name='height',
            field=models.IntegerField(null=True),
        ),
        migrations.AlterField(
            model_name='tanks',
            name='volume',
            field=models.IntegerField(null=True),
        ),
    ]
