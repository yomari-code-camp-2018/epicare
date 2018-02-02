from flask import Blueprint, jsonify, request
from routes.models import User, db
mod = Blueprint('login', __name__)


@mod.route("/login", methods=["GET"])
def login():

    data = request.args

    user = User.query.filter_by(email=data.get('email')).first()
    if user is None:
        return jsonify(status=0)

    else:

        password = data.get('password')

        if user.check_password(password):
            username = user.firstname + user.lastname
            return jsonify(status='success', token=user.token, doctor=user.type_, name=username)
        else:
            return jsonify(status=0)
