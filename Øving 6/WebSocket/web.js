const net = require('net');
const crypto = require('crypto')

const connections=[]

// Incomplete WebSocket server
const wsServer = net.createServer((connection) => {
    console.log('Client connected');
    connection.on('data', (data) => {
      console.log(data)
      console.log('Data received from client:\n', data.toString());
      if(data.toString().split('\n')[0].includes('GET / HTTP')){
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
    	  
    
        connection.write(response)
        connections.push(connection)
        console.log('\n' + response)
    
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
      try{
        const messageToClient = prepareMessage(message)
        for (let index = 0; index < connections.length; index++) {
          connections[index].write(messageToClient)
        }
    } catch (error){
        //When JSON can't parse the message, it means the client has disconnected.
        
    }
      }
		})
  
    connection.on('end', () => {
      console.log('Client disconnected');
    });
  });

  
  wsServer.listen(3001, "localhost", () => {
    console.log('WebSocket server listening on port 3001');
  });

  function prepareMessage(message){
    const msg = Buffer.from(message)
    const msgSize = msg.length

    let dataFrameBuffer

    const firstByte = 0x80 | 0x01
    if (msgSize <= 125){
        const bytes = [firstByte]
        dataFrameBuffer = Buffer.from(bytes.concat(msgSize))
    } else {
        throw new Error("Message too long.")
    }
    const totalLength = dataFrameBuffer.byteLength + msgSize

    const dataFrameResponse = Buffer.allocUnsafe(totalLength)
    let offset = 0
    for (const buffer of [dataFrameBuffer, msg]){
        dataFrameResponse.set(buffer, offset)
        offset += buffer.length
    }
    return dataFrameResponse
}