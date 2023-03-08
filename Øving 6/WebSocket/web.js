const net = require('net');
const crypto = require('crypto')


// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
	console.log("created http: " + connection.localPort)
  connection.on('data', () => {
			
	let content = `
		<!DOCTYPE html>
		<html>
		<head>
			<meta charset="UTF-8" />
		</head>
		<body>
			<h1>WebSocket test page</h1>
			<div class = "messageSender">
			<label for="message"> Message: </label>
			<input id="message" placeholder="Type your message here">
			<button onclick = "sendData()" type="submit">Send</button>
			</div>

			<script>
			const input = document.getElementById("message")
			let ws = new WebSocket('ws://localhost:3001');
			ws.onmessage = event => alert('Message from server: ' + event.data);
			ws.onopen = () => ws.send('hello');

			function sendData(){
				if (ws.readyState === WebSocket.OPEN) {
					ws.send(input.value);
				} else {
					console.log('Websocket is not connected')
				}	
			}
			</script>
			<style>
			body{
				display:flex;
				flex-direction: column;
				align-items:center;
			}
			.messageSender{

			}
			</style>
		</body>
		</html>
		`
	connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);        
  });
  connection.on('end', () =>{
	console.log("http connection ended");
  })
});

httpServer.listen(3000, '127.0.0.1', () => {
  console.log('HTTP server listening on port 3000');
});

// Incomplete WebSocket server
const wsServer = net.createServer((connection) => {
    console.log('Client connected');
    connection.on('data', (data) => {
      	if(data.toString().split('\n')[0].includes('GET / HTTP')){
			console.log('Data received from client: ', data.toString());
			const info = data.toString().split('\n')
			var result = ""
			for(let i = 0; i<info.length; i++){
				if(info[i].includes('Sec-WebSocket-Key')){
					result = info[i]
				}
			}

			const clientKey = result.split(':')[1].trim()
			const magicString = '258EAFA5-E914-47DA-95CA-C5AB0DC85B11';
    		const serverKey = crypto.createHash('sha1')
    		  .update(clientKey + magicString)
    		  .digest('base64');

    		const response = `HTTP/1.1 101 Switching Protocols\r\n`
    		  + `Upgrade: websocket\r\n`
    		  + `Connection: Upgrade\r\n`
    		  + `Sec-WebSocket-Accept: ${serverKey}\r\n\r\n`;
    		connection.write(response);
		}
		else{
			let length = data[1] & 127;
			let maskStart = 2;
			let dataStart = maskStart + 4;
			let message = ""
			for (let i = dataStart; i < dataStart + length; i++) {
				let byte = data[i] ^ data[maskStart + ((i - dataStart) % 4)];
				message+=String.fromCharCode(byte)
			}
			console.log(message)
			console.log(connection.readyState)
			connection.write(message)
		}
    });
  
    connection.on('end', () => {
      console.log('Client disconnected');
    });
  });

  wsServer.on('error', (error) => {
    console.error('Error: ', error);
  });
  wsServer.listen(3001, () => {
    console.log('WebSocket server listening on port 3001');
  });
