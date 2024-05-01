# Generated by Django 4.2.6 on 2023-12-13 10:14

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Container',
            fields=[
                ('Id', models.IntegerField(primary_key=True, serialize=False, verbose_name='Id')),
                ('Name', models.TextField()),
            ],
        ),
        migrations.CreateModel(
            name='Tanks',
            fields=[
                ('Id', models.IntegerField(primary_key=True, serialize=False)),
                ('Name', models.TextField()),
                ('height', models.IntegerField()),
                ('volume', models.IntegerField()),
                ('Container_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='home.container')),
            ],
        ),
        migrations.CreateModel(
            name='Tank_data',
            fields=[
                ('Id', models.IntegerField(primary_key=True, serialize=False)),
                ('Date', models.DateField()),
                ('Time', models.TimeField()),
                ('water_level', models.IntegerField()),
                ('Tank_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='home.tanks')),
            ],
        ),
    ]
