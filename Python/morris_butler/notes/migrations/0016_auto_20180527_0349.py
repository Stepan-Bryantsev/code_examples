# Generated by Django 2.0.5 on 2018-05-27 00:49

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('notes', '0015_auto_20180527_0326'),
    ]

    operations = [
        migrations.AlterField(
            model_name='notes',
            name='added_time',
            field=models.DateTimeField(auto_now_add=True),
        ),
    ]
