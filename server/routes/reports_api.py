from flask import Blueprint, jsonify, request, abort
from routes.models import Reports, db, User, Disease, Timestmp
import datetime
mod = Blueprint('reports_api', __name__)


@mod.route("/post", methods=["GET"])
def report():
    print('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa')
    data = request.args
    if User.query.filter_by(token=data['token']).first():

        Disease.query.filter_by(
            uid=data.get('disease_id')).first().total_reports += 1
        db.session.commit()
        criteria = Reports.query.filter((Reports.disease_id == data.get('disease_id')) & (
            Reports.gender == data.get('gender')) & (Reports.age == data.get(
                'age')) & (Reports.location == data.get('location'))).first()
        if criteria:
            criteria.total_reports += 1
            db.session.commit()
            time_manager(data)
            return jsonify(status='success')
        else:
            # create new Row if there is not exsisting row matching the criteria
            report = Reports(age=data.get('age'), gender=data.get('gender'), total_reports=1,
                             disease=Disease.query.filter_by(uid=data.get('disease_id')).first(), location=data.get('location'))
            db.session.add(report)
            db.session.commit()

            time_manager(data)
            return jsonify(status='success')
    abort(500)


def time_manager(data):
    TIME_FORMAT = '%Y-%m-%d'

    time_ = datetime.date.today()
    # - datetime.timedelta(days=3)
    db_timestmp = Timestmp.query.filter(
        (Timestmp.date_ == time_) & (Timestmp.disease_id == data.get('disease_id'))).first()

    if db_timestmp is not None:
        db_timestmp.total_reports += 1

        db.session.commit()
    else:
        datm = Timestmp(diseases=Disease.query.filter_by(
            uid=data.get('disease_id')).first(), date_=time_, total_reports=1)
        db.session.add(datm)
        db.session.commit()
