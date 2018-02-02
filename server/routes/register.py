from flask import Blueprint, request, jsonify
from routes.models import User, db
import os
from itsdangerous import Signer, URLSafeTimedSerializer, SignatureExpired
mod = Blueprint('register', __name__)


@mod.route("/register", methods=["POST"])
def signup():

    data = request.get_json()
    app_root = os.getcwd()
    s = Signer('superpowerfullkey')
    token = s.sign(data['email'].encode()).decode()
    token = token[::-1].split('.')[0][::-1]

    if 'type' not in data:
        data['type'] = False
        image_path = None
    else:
        import base64
        byte_image = base64.b64decode(data['image'])
        if not os.path.isdir(os.path.join(app_root, "images/")):
            os.mkdir(os.path.join(app_root, "images/"))
        image_path = os.path.join(os.path.join(
            app_root, "images/"), data['first_name'] + ' ' + data['last_name'] + '.jpg')
        with open(image_path, 'wb') as f:
            f.write(byte_image)

    newuser = User(data['first_name'], data['last_name'],
                   data['email'], data['password'], token, data['type'], filepath_=image_path if image_path is not None else None)

    db.session.add(newuser)
    db.session.commit()

    return jsonify(status='success', token=token)
