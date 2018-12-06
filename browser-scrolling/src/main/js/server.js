const connect = require('connect');
const path = require('path');
const serveStatic = require('serve-static');

connect()
  .use(serveStatic(path.join(__dirname, '../../../build/resources/main/')))
  .listen(process.env.PORT || 3000, () => console.log('Server running on %d...', process.env.PORT || 3000));
