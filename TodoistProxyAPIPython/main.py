# gcloud functions deploy test-to-delete --entry-point simple_cloud_function --region europe-west1 --runtime python38 --trigger-http --allow-unauthenticated

def simple_cloud_function(request):
    return '''<h1>TEST</h1>'''
