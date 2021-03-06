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
        self.ACCESS_TOKEN_REGEX = r'^[0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}$'
        self.BASE_URL = 'https://europe-west1-websitequickadding4todoisttest.cloudfunctions.net/access-token'

    def tearDown(self):
        pass

    def assert_json_response(self, json_response):
        self.assertIsNotNone(json_response, 'Response is None.')
        self.assertRegex(json_response['access-token'], self.ACCESS_TOKEN_REGEX, 'Correct Access Token, using regex, has not been found.')

    def get_request_mock(self, data, method_value='POST'):
        return Mock(get_json=Mock(return_value=data), args=data, method=method_value)

    @pytest.mark.unittest
    def test_local_access_token(self):
        data = {'code': str(uuid.uuid4()), 'state': str(uuid.uuid4())}

        # Call tested function
        json_response = access_token(self.get_request_mock(data))
        self.assert_json_response(json_response)

    @pytest.mark.unittest
    def test_local_access_token_with_GET_method(self):
        data = {'code': str(uuid.uuid4()), 'state': str(uuid.uuid4())}

        # Call tested function
        with self.assertRaises(HTTPException) as http_error:
            access_token(self.get_request_mock(data, 'GET'))

        self.assertEqual(http_error.exception.code, 403)
        self.assertEqual(http_error.exception.description, 'method POST only')
        
    @pytest.mark.unittest
    def test_local_access_token_no_code(self):
        data = {'state': str(uuid.uuid4())}

        # Call tested function
        with self.assertRaises(HTTPException) as http_error:
            access_token(self.get_request_mock(data))

        self.assertEqual(http_error.exception.code, 400)
        self.assertEqual(http_error.exception.description, 'code cannot be null')

    @pytest.mark.unittest
    def test_local_access_token_no_state(self):
        data = {'code': str(uuid.uuid4())}

        # Call tested function
        with self.assertRaises(HTTPException) as http_error:
            access_token(self.get_request_mock(data))

        self.assertEqual(http_error.exception.code, 400)
        self.assertEqual(http_error.exception.description, 'state cannot be null')

    @pytest.mark.unittest
    def test_local_access_token_no_code_no_state(self):
        data = {}

        # Call tested function
        with self.assertRaises(HTTPException) as http_error:
            access_token(self.get_request_mock(data))

        self.assertEqual(http_error.exception.code, 400)
        self.assertEqual(http_error.exception.description, 'code cannot be null')



    ############## DEPLOYMENT TEST ##############

    @pytest.mark.deploymenttest
    def test_remote_access_token(self):

        assert self.BASE_URL is not None
        data = {'code': str(uuid.uuid4()), 'state': str(uuid.uuid4())}

        response = requests.post(
            '{}'.format(self.BASE_URL),
            json=data
        )

        self.assertEqual(response.status_code, 200)
        self.assert_json_response(json.loads(response.text))

    @pytest.mark.deploymenttest
    def test_remote_access_token_with_GET_method(self):

        assert self.BASE_URL is not None
        response = requests.get('{}'.format(self.BASE_URL))

        self.assertEqual(response.status_code, 403)
        self.assertIn('method POST only', response.text)
