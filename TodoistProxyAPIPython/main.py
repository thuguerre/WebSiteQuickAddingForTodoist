from flask import abort
import uuid

def access_token(request):
    #TODO : document this method

    request_json = request.get_json(silent=True)
    request_args = request.args

    if request.method != 'POST':
        abort(403, 'method POST only')

    if request_json and 'code' in request_json:
        code = request_json['code']
    else:
        abort(400, description="code cannot be null")

    if request_json and 'state' in request_json:
        state = request_json['state']
    else:
        abort(400, description="state cannot be null")

    #TODO : call Todoist Service
    response = {'access-token': str(uuid.uuid4())}

    #TODO : has to return under JSON format?
    return response
