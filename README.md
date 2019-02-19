# TourVision

Theme: Artificial Intelligence and Tourism
Idea: An android app that uses Artificial intelligence to act as a tour guide. 
Explanation:
1.	The app will plan the whole tour for a tourist such as booking tickets, reservations and schedule. This will be done by using AI to optimize cost, so that tourists can have maximum benefit at minimum time and cost. The app will schedule the tour-
a.	The tourist will input their time for the tour lets say from 19 to 21 March and enter their destination as Udaipur.
b.	The app will find tickets, reservations, etc. at best price for the time mentioned above.
c.	Then the app will schedule the tour. It will find the top attractions/landmarks/places in the city (Udaipur in this example) and schedule the tour across the various days. It will also choose the time to visit the respective places appropriately. Say there is a sunset point, then the app will fix a time around evening.
d.	The app will calculate shortest paths between the destinations and plan the trip appropriately.
e.	The app will also find shops to buy souvenirs, gift shops and other places the tourist might like.
f.	The tourist can select where he wants to go from the list of the destinations.
2.	Once the tourist reaches the destination, the app will start a tour of that place. Voice recognition system can guide the tourist and also tell them the best places to take pictures. The tourist can use the camera to take a picture of various places and the app will tell the tourist about the history of the place.
3.	After the tour, the tourist can add feedback. The app will use this feedback to improve the next tour.
4.	The tourist can also post any complaints regarding any particular place he did not like. For example- he/she may have found the place dirty (littered with garbage). This data can be used by the government to improve the tourist attractions.

Prototype includes :

TourVision app:
This demonstrates that the user can input details such as tour destination, date range of tour and the app will schedule the tour for the tourist by making clusters of the landmarks in the destination city. Number of clusters will be equal to the number of days. One cluster will be scheduled for one day. The clusters are calculated by a machine learning algorithm called K-means clustering. This ensures that wastage of time is minimized.

TensorFlow Demo:

https://drive.google.com/open?id=1qfleUKLq1oBK_TDuR32tFtKW-dKxS8Ox
This app is the demo TensorFlow app in which we have trained our own classifier. This classifier can classify images and label them into five categories- mouse, keyboard, me(kuldeep) and my two friends(Mridul and bhanu).
This demonstrates the ability to train the app to recognize the various points of interests such as statues, antiques, etc. In this way the app can act as a tour guide. The app can play audio files in the local language of the tourist to describe the tourist destination.
The app will show a map of a tourist location and a path drawn on it. It will guide the tourist across the destination.
