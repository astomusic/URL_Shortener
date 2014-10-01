var fs = require('fs');
//var data = fs.readFileSync('./appendonly.aof', 'utf8');
var readline = require('readline');
var mysql = require('mysql');

var connection = mysql.createConnection({
    host    :'localhost',
    port : 3306,
    user : 'urlshortener',
    password : 'db1004',
    database:'url_shortener'
});

connection.connect();

var rd = readline.createInterface({
    input: fs.createReadStream('./appendonly.aof'),
    output: process.stdout,
    terminal: false
});

var currentCmd = "";
var offset = -1;
var key = "";
var value = "";
var lineNum = 0;
var querySucceed = 0;


rd.on('line', function(line) {
    if(line == "set") {
    	currentCmd =  "set";
    	offset = 4;
    }

    if(offset == 2 ) {
    	key = line;
    }

    if(offset == 0 ) {
    	value = line;
    	insertInto(key,value);
        lineNum++;
    }

   	if(offset > -1) {
   	   	offset--;
   	}
   	console.log(line);
});

rd.on('close', function() {
    console.log(lineNum);
});

function insertInto(key, value) {    
	console.log("key: " + key + ", value :" + value);
	console.log("QUERY: "+ 'insert into url_data values("' + key + '","' + value + '")'); 
    var sql = 'insert into url_data values("' + key + '","' + value + '")';
    //console.log(connection);
    var query = connection.query(sql, function(err,result){
        if (err) {
            console.log("인서트 에러딧!");
            console.error(err);
            throw err;
        } else {
            console.log("인서트 성공!");
            querySucceed++;
        }
        if(querySucceed === lineNum) {
            process.exit(1);
            connection.end();
        }
    });
}