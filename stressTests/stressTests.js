'use strict';

const configuration = require('./testData.json');
const expect = require('chai').expect;
const loadtest = require('loadtest');
const testDefinitions = configuration.stressTests;
const q = require('q');

describe('Stress tests', function () {
  const address = "http://default-environment.pmpnqmqig7.eu-west-1.elasticbeanstalk.com";

  it('First test', (done) => {
    let endpointConfig = testDefinitions[0];
    endpointConfig.options.url = `${address}${endpointConfig.relativeUrl}`;

    loadtest.loadTest(endpointConfig.options, function (error, result) {
      if (error) {
        console.log('Tests failed');
        done();        
      }
      // show output
      console.log(endpointConfig.name);
      console.log(result);

      //validate         
      expect(result.totalErrors).to.be.within(...endpointConfig.thresholds.errors);
      expect(result.meanLatencyMs).to.be.within(...endpointConfig.thresholds.meanLatency);
      done();
    });
  });
});
