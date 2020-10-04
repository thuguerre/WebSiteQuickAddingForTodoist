from main import access_token
import pytest
import unittest
import requests
import uuid
import json
from unittest.mock import Mock
from werkzeug.exceptions import HTTPException

class TestMain(unittest.TestCase):

    def setUp(self):
        pass

    def tearDown(self):
        pass

    @pytest.mark.unittest
    def test_local_access_token(self):
        data = {'code': str(uuid.uuid4()), 'state': str(uuid.uuid4())}
        req = Mock(get_json=Mock(return_value=data), args=data)

        # Call tested function
        json_response = access_token(req)

        self.assertIsNotNone(json_response)
        #TODO : test JSON content

    @pytest.mark.unittest
    def test_local_access_token_no_code(self):
        data = {'state': str(uuid.uuid4())}
        req = Mock(get_json=Mock(return_value=data), args=data)

        # Call tested function
        with self.assertRaises(HTTPException) as http_error:
            access_token(req)

        self.assertEqual(http_error.exception.code, 400)
        self.assertEqual(http_error.exception.description, 'code cannot be null')

    @pytest.mark.unittest
    def test_local_access_token_no_state(self):
        data = {'code': str(uuid.uuid4())}
        req = Mock(get_json=Mock(return_value=data), args=data)

        # Call tested function
        with self.assertRaises(HTTPException) as http_error:
            access_token(req)

        self.assertEqual(http_error.exception.code, 400)
        self.assertEqual(http_error.exception.description, 'state cannot be null')

    @pytest.mark.unittest
    def test_local_access_token_no_code_no_state(self):
        data = {}
        req = Mock(get_json=Mock(return_value=data), args=data)

        # Call tested function
        with self.assertRaises(HTTPException) as http_error:
            access_token(req)

        self.assertEqual(http_error.exception.code, 400)
        self.assertEqual(http_error.exception.description, 'code cannot be null')



    ############## DEPLOYMENT TEST ##############

    @pytest.mark.deploymenttest
    def test_remote_access_token(self):
        BASE_URL = 'https://europe-west1-websitequickadding4todoisttest.cloudfunctions.net/access-token'
        assert BASE_URL is not None

        data = {'code': str(uuid.uuid4()), 'state': str(uuid.uuid4())}

        res = requests.post(
            '{}'.format(BASE_URL),
            json=data
        )
        assert res.text == '''<h1>TEST2</h1>'''
        #TODO : assert code response = 200
