README:

CS449 Final Project - PlateScan


This app is Plate Scan. It is an app that will allow you to take pictures of car number plates to verify if the car is a suspicious car or not. It's an android application that scans license plates, converts to text, and request data from an API.

User Profile:
	When you first open the app, you will be asked to make an account. The account is for the purpose of saving the images you click in the app. It will save the images under you account incase the number plate that you had taken an image of shows up on the rader a while after you have clicked the image.
	
Plate Recognition:
	The camera in the app will detect the license plate when you hold the camera up to the car and when you click the image, it will take the image and convert it to text so that it can get the numbers and letters of the license plate and return a string variable. The string will then be sent to an external API to detect any notices on the license number. If there is nothing on the license plate then you will get nothing back and you image will be saved for later use. 
	
Push Notifications:
	Once you have created an account and clicked some images of number plates and when a reply comes from the API that the number plate clicked is a suspicious number plate, then your account will get a notification that the image you have clicked was an image of a car that was needed so you will get a reward for the image.