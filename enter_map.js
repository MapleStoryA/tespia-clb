var ip = '127.0.0.1';
client.connect(ip, 8484);
client.login('admin', 'admin', '1111', 0, 0);

function onConnectInGame(){
 controller.log('Character is connected in game.')
}