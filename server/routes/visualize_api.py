from flask import Blueprint, jsonify
from routes.models import db, Timestmp, Reports

mod = Blueprint('retrive_api', __name__)


@mod.route("/graph/<int:number>", methods=["GET"])
def retrive(number):
    disease = []

    disease_id = [r.disease_id for r in db.session.query(
        Reports.disease_id).distinct()]
    diseases = Timestmp.query.filter_by(disease_id=number).all()
    age_counts = 0
    age_list = []
    dates = [[r.date_.strftime('%Y-%m-%d'), r.total_reports] for r in diseases]

    reports_filter = Reports.query.filter_by(disease_id=number).all()
    ages = list(set([i.age for i in reports_filter]))
    female_count = 0
    male_count = 0
    for i in ages:
        for j in Reports.query.filter((Reports.disease_id == number) & (Reports.age == i)).all():
            age_counts += j.total_reports
            if j.gender == 'F':
                female_count += j.total_reports
            elif j.gender == 'M':
                male_count += j.total_reports
        age_list.append([i, age_counts])
        age_counts = 0

    return jsonify(disease_bargraph=dates, age_piechart=age_list, gender_bargraph={'M': male_count, 'F': female_count})
