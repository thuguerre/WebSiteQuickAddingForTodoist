from main import simple_cloud_function
import pytest
import unittest
import requests
import uuid
from unittest.mock import Mock

# https://cloud.google.com/functions/docs/testing/test-http?hl=fr


class TestMain(unittest.TestCase):

    def setUp(self):
        pass

    def tearDown(self):
        pass

    @pytest.mark.unittest
    def test_local_simple_cloud_function(self):
        data = {}
        req = Mock(get_json=Mock(return_value=data), args=data)

        # Call tested function
        assert simple_cloud_function(req) == '''<h1>TEST1</h1>'''

    @pytest.mark.deploymenttest
    def test_remote_simple_cloud_function(self):
        BASE_URL = 'https://europe-west1-websitequickadding4todoisttest.cloudfunctions.net/test-to-delete'
        assert BASE_URL is not None

        name = str(uuid.uuid4())
        res = requests.post(
            '{}/hello_http'.format(BASE_URL),
            json={'name': name}
        )
        assert res.text == '''<h1>TEST1</h1>'''
