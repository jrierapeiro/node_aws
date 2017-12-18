'use strict';

module.exports.handler = (event, context, callback) => {
  // static list of items
  let items = [
    { name: 'libro', id: 'A001' },
    { name: 'libretra', id: 'A002' },
    { name: 'camara', id: 'A003' },
    { name: 'teclado', id: 'A004' },
    { name: 'wifi', id: 'A005' },
    { name: 'word', id: 'A006' },
    { name: 'lamnda', id: 'A007' }
  ];

  const searchParameter = event.queryStringParameters ? event.queryStringParameters.s : null;
  let resultItems = searchParameter ? items.filter((i) => { return i.name.startsWith(searchParameter); }) : items;
  
  var response = {
    statusCode: 200,
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(resultItems || [])
  };

  callback(null, response);
};
