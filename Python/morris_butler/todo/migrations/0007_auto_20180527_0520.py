# Generated by Django 2.0.5 on 2018-05-27 02:20

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('todo', '0006_auto_20180525_2046'),
    ]

    operations = [
        migrations.CreateModel(
            name='TodosFolder',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(default='Title', max_length=100)),
                ('done', models.BooleanField(default=False)),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.RenameField(
            model_name='todos',
            old_name='added_date_and_time',
            new_name='added_time',
        ),
        migrations.AlterField(
            model_name='todos',
            name='title',
            field=models.CharField(default='title', max_length=100),
        ),
        migrations.AddField(
            model_name='todos',
            name='folder',
            field=models.ForeignKey(default=None, null=True, on_delete=django.db.models.deletion.CASCADE, to='todo.TodosFolder'),
        ),
    ]
