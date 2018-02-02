from flask_sqlalchemy import SQLAlchemy
from werkzeug import generate_password_hash, check_password_hash

import datetime
import json
db = SQLAlchemy()


# def utc_2_local():
#     TIME_FORMAT = '%Y-%m-%d %H:%M:%S'

#     utc = datetime.datetime.utcnow()#     utc = datetime.datetime.utcnow()


#     timestamp = calendar.timegm(utc.timetuple())
#     local = datetime.datetime.fromtimestamp(timestamp)
#     return local


class User(db.Model):
    __tablename__ = 'users'
    uid = db.Column(db.Integer, primary_key=True)
    firstname = db.Column(db.String(100))
    lastname = db.Column(db.String(100))
    email = db.Column(db.String(120), unique=True)
    pwdhash = db.Column(db.String(100))
    token = db.Column(db.String(100), unique=True)
    type_ = db.Column(db.Boolean())
    filepath = db.Column(db.Text())

    def __init__(self, firstname, lastname, email, password, token, type_, filepath_=None):
        self.firstname = firstname.title()
        self.lastname = lastname.title()
        self.email = email.lower()
        self.token = token
        self.type_ = type_
        print(filepath_)

        filepath = filepath_

        self.set_password(password)

    def set_password(self, password):
        self.pwdhash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.pwdhash, password)


class Reports(db.Model):
    __tablename__ = 'reports'
    uid = db.Column('uid', db.Integer, primary_key=True)
    disease_id = db.Column('disease_id', db.Integer,
                           db.ForeignKey('disease_info.uid'))
    age = db.Column('age', db.String(10))
    gender = db.Column('gender', db.String(5))
    location = db.Column('location', db.String(100))
    total_reports = db.Column('total_reports', db.Integer)


class Timestmp(db.Model):
    __tablename__ = 'timestmp'

    uid = db.Column('uid', db.Integer, primary_key=True)
    date_ = db.Column(
        'date', db.Date)
    disease_id = db.Column('disease_id', db.Integer,
                           db.ForeignKey('disease_info.uid'))
    total_reports = db.Column('total_reports', db.Integer)


class Disease(db.Model):
    __tablename__ = 'disease_info'
    uid = db.Column('uid', db.Integer, primary_key=True)
    name = db.Column('disease_name', db.String(100))
    description = db.Column('disease_description', db.Text())
    symptom = db.Column('disease_symptom', db.Text())
    cause = db.Column('disease_cause', db.Text())
    prevention = db.Column('disease_prevention', db.Text())
    total_reports = db.Column('total_reports', db.Integer)
    reports = db.relationship(
        'Reports', backref='disease', lazy='dynamic')
    get_time = db.relationship('Timestmp', backref='diseases', lazy='dynamic')
    image_url = db.Column('image_url', db.Text())
