from django import forms
from ckeditor_uploader.widgets import CKEditorUploadingWidget
from ckeditor_uploader.fields import RichTextUploadingFormField


class SaveTodoForm(forms.Form):
    todo_title = forms.CharField(label='Enter title', max_length=100,
                                 widget=forms.TextInput(attrs={'form': 'add_todo_form', 'class': 'form-control', 'placeholder': 'Название'}))
    todo_time = forms.TimeField(widget=forms.TimeInput(attrs={'form': 'add_todo_form', 'class': 'form-control', 'type': 'time'}))
    todo_deadline = forms.DateField(widget=forms.DateInput(attrs={'form': 'add_todo_form', 'class': 'form-control', 'type': 'date'}))
    todo_priority = forms.IntegerField(widget=forms.HiddenInput())


class EditTodoForm(forms.Form):
    todo_edit_title = forms.CharField(label='Enter title', max_length=100,
                                      widget=forms.TextInput(attrs={'form': 'edit_todo_form', 'class': 'form-control', 'placeholder': 'Напоминалочка'}))
    todo_edit_time = forms.TimeField(widget=forms.TimeInput(attrs={'form': 'edit_todo_form', 'class': 'form-control', 'type': 'time'}))
    todo_edit_deadline = forms.DateField(widget=forms.DateInput(attrs={'form': 'edit_todo_form', 'class': 'form-control', 'type': 'date'}))
    todo_edit_priority = forms.IntegerField(widget=forms.HiddenInput())