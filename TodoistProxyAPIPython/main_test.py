from main import simple_cloud_function
import unittest
from unittest.mock import Mock

# https://cloud.google.com/functions/docs/testing/test-http?hl=fr

class TestMain(unittest.TestCase):

    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_simple_cloud_function(self):
        data = {}
        req = Mock(get_json=Mock(return_value=data), args=data)

        # Call tested function
        assert simple_cloud_function(req) == '''<h1>TEST</h1>'''

