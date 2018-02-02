from flask import Flask, render_template, request, session, redirect, url_for, abort, jsonify
from routes.models import db, User, Timestmp, Reports, Disease

# from flask_admin import Admin
# from flask_admin.contrib.sqla import ModelView

app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:0@localhost:5432/epicare'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.secret_key = "thisisyunik"

db.init_app(app)

from routes import login, register, reports_api, home_api, visualize_api

app.register_blueprint(login.mod)
app.register_blueprint(register.mod)
app.register_blueprint(reports_api.mod, url_prefix='/api')
app.register_blueprint(home_api.mod, url_prefix='/api')
app.register_blueprint(visualize_api.mod, url_prefix='/api')


# admin.add_view(ModelView(User, db.session))

# admin.add_view(ModelView(Timestmp, db.session))

# admin.add_view(ModelView(Reports, db.session))

# admin.add_view(ModelView(Disease, db.session))
