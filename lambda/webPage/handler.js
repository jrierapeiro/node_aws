'use strict';

module.exports.handler = (event, context, callback) => {



  var AWS = require('aws-sdk');
  var s3 = new AWS.S3();
  AWS.config.update({ region: 'eu-west-1' });
  var params = {
    Bucket: 'jariepei-lambda',
    Key: 'index.html'
  };

  s3.getObject(params, function (err, data) {
    if (err) throw err;
    console.log(data);
    console.log("Key was", params.Key);

    var response = {
      statusCode: 200,
      headers: {
        'Content-Type': 'text/html',
      },
      body: data.Body.toString('utf-8')
    };

    // callback is sending HTML back
    callback(null, response);
  });
};
