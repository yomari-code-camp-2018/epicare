from flask import Blueprint, jsonify, request, abort
from routes.models import Reports, db, User, Disease

mod = Blueprint('home_api', __name__)


@mod.route("/home", methods=["GET"])
def report():
    report_arr = []
    location = request.args.get("location")
    max_report = 0
    Data = {}
    top_results = []
    json_return = {}
    id = 0
    disease_id = [r.disease_id for r in db.session.query(
        Reports.disease_id).distinct()]

    for i, d in enumerate(disease_id, 1):

        lists = Reports.query.filter(
            (Reports.location == location) & (Reports.disease_id == d)).all()
        if lists:
            for row in lists:
                max_report += row.total_reports

            report_arr.append([d, max_report])

            max_report = 0
    for i in range(len(report_arr) - 1):
        for j in range(i + 1, len(report_arr)):
            if report_arr[i][1] < report_arr[j][1]:
                report_arr[i], report_arr[j] = report_arr[j], report_arr[i]

    top_reported_disease = [i[0] for i in report_arr]

    # print(top_reported_disease)
    # print(report_arr)
    for i, d in enumerate(top_reported_disease):
        diseases = Disease.query.filter_by(uid=d).first()
        json_return['disease_id'] = top_reported_disease[i]

        json_return['name'] = diseases.name
        json_return['count'] = report_arr[i][1]
        json_return['symptom'] = diseases.symptom
        json_return['description'] = diseases.description
        json_return['prevention'] = diseases.prevention
        json_return['image_url'] = diseases.image_url if diseases.image_url else '0'
        top_results.append(json_return)
        json_return = {}
    return jsonify(disease=top_results)

    abort(500)
