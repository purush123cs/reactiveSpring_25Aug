const express = require('express')
const app = express()
const bodyParser = require("body-parser");
const port = 9001

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.post('/employees', (req, res) => {
	console.log("entered /1 endpoint");
	const mockResponse = {
							"employeeId": "1",                  
							"employeeName": "purush",
							"departmentId": "DEPT_01"
                         }
	console.log("res body:"+JSON.stringify(mockResponse));
	res.status(200).json(mockResponse);
})

app.get('/employees/1', (req, res) => {
	console.log("entered /1 endpoint");
	const mockResponse = {
							"employeeId": "1",                  
							"employeeName": "purush",
							"departmentId": "DEPT_01"
                         }
	console.log("res body:"+JSON.stringify(mockResponse));					 
	res.status(200).json(mockResponse);
})

app.get('/department/DEPT_01', (req, res) => {
	console.log("entered /1 endpoint");
	const mockResponse = {
							"departmentId": "DEPT_01",                  
							"departmentName": "Admin"
                         }
	console.log("res body:"+JSON.stringify(mockResponse));					 
	res.status(200).json(mockResponse);
})

app.listen(port, () => console.log(`Example app listening at http://localhost:${port}`))