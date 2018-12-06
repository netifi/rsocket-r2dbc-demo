const static = require('node-static');

//
// Create a node-static server to serve the current directory
//
const file = new static.Server('./build/resources/main', { cache: 7200, indexFile: "index.html" });

require('http').createServer(function (request, response) {
    file.serve(request, response, function (err, res) {
        if (err) { // An error as occured
            console.error("> Error serving " + request.url + " - " + err.message);
            response.writeHead(err.status, err.headers);
            response.end();
        } else { // The file was served successfully
            console.log("> " + request.url + " - " + res.message);
        }
    });
}).listen(process.env.PORT || 3000);

console.log("> node-static is listening on http://127.0.0.1:3000");
