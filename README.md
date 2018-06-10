# SST-Smart_Score_Tracker-
(가천대학교 소프트웨어학과 졸업작품 5조) 안기훈, 김기훈, 유지민


### **1. Project introduction**
__Problem__:
1. Hard billiards rule for beginners
2. Discomfort to calculate score manually in Billiards which score change frequently
3.  A uniform service(Low customer loyalty)

__Solution__ :
- Provide convenience and new service to users by using image processing technology
-  Activate the Billiard market from the younger generation

__System overview__:
![텍스트](https://github.com/kkkHoon/SST-Smart_Score_Tracker-/blob/master/Img/system_overview.png)

  
 __System structure__:  
![텍스트](https://github.com/kkkHoon/SST-Smart_Score_Tracker-/blob/master/Img/system_overview2.png) 
  
  

### **2. Installation environments**
__OS__ : Linux(server), Android(client)  
__Language__: Python, JAVA  
__required program__: python 3, Android Studio, VLC  
__hardware__: Raspberry pi 3, Raspberry pi camera module V2, Tablet or smart phone on Android  

  
  

### **3. API**

###  **users**
|      Field     | Type    |                                 Description                                |
|:--------------:|---------|:--------------------------------------------------------------------------:|
|    user_name   |  String |                                 user's name                                |
|   user_score   | Integer |                            current user's score                            |
| user_max_scroe | Integer |                              user's goal score                             |
|   start_time   |   long  |                       The first start time of playing                      |
|    stop_time   |   long  |                          The last time of playing                          |
|   base_value   |   long  | To adjust start point on each timer (I used Chronometer class in ListView) |
|       end      | boolean |               check value whether player reach on one's goal               |
|      turn      | boolean |          check value whether player is now playing or not(waiting)         |

### **Class table on Andorid**
|          Class         |     extend / implements    |                                                                      Description                                                                     |
|:----------------------:|:--------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------:|
|        user_info       |    implements Parcelable   |                                                    store user's information(reference users table)                                                   |
|       users_info       |    implements Parcelable   |                                                    Container class that binds all user_info class                                                    |
|  individual_scoreboard |  extends AppCompatActivity |                            Connect and Communicate with server Do on-demand streaming service Measure total game play time                           |
|   fragment_scoreboard  |       extends Fragment     | Fragment class in an individual_scoreboard class Manage score records for all player. Bind each player's score view and user_info data using adapter |
|   adpater_percentage   |     extends BaseAdapter    |         Store user's information and view list. Adjust base time of Timer class to maintain each player's play time.         |
|          Timer         |     extends Chronometer    |              Each player's personal timer The timer only works when player plays the game (each player's timer can has a different time)             |

  
  
  
### **4. Sample code** 
```
private void update_view(View view, int position) throws Exception{
        user_info user_data = (user_info) getItem(position);
        if (user_data == null)
            throw new Exception();

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        TextView textView = (TextView) view.findViewById(R.id.score_info);
        ImageButton imageButton = (ImageButton)view.findViewById(R.id.user_info);
        Timer personal_timer = (Timer)view.findViewById(R.id.item_timer);

        if(user_data.getTurn()){ // playing
            if(imageButton.getDrawable() == null || !imageButton.getDrawable().equals(playing)) {
                if(Build.VERSION.SDK_INT >= 16)
                    imageButton.setBackground(playing);
                else
                    imageButton.setBackgroundDrawable(playing);
                long value = user_data.getBase_value();
                //user_data.setPlus_time((elapsedRealtime() - user_data.getStop_time()));
                personal_timer.setBase(value);
                personal_timer.start();
            }
        }
        else{
            if(imageButton.getDrawable() == null || !imageButton.getDrawable().equals(waiting)) {
                if(Build.VERSION.SDK_INT >= 16)
                    imageButton.setBackground(waiting);
                else
                    imageButton.setBackgroundDrawable(waiting);
                long value = user_data.getBase_value() + (elapsedRealtime() - user_data.getStop_time());
                //user_data.setPlus_time((elapsedRealtime() - user_data.getStop_time()));
                personal_timer.setBase(value);
                personal_timer.start();
                personal_timer.stop();
            }
        }
```
Chronometer class is in ListView. So, whenever listview is refreshed(even scrolled),  
Chronometer base became current time(__initialized to 00:00__)  
so, I should have to adjust each chronometer's base value using __base_value and stop_time field__  

  
  
### **5. Test**
__If you want to test first, use simulator program.__  

1. run server.py on raspberry pi
``` python3 server.py ```
2. run Client program(android) on tablet or smart phone
3. If you connected with server successfully, then you can see server GUI
(Insert picture here)
4. push buttons on GUI

__server.py structure__
![텍스트](https://github.com/kkkHoon/SST-Smart_Score_Tracker-/blob/master/Img/simulator_structure.png)

- producer thread diagram activity  
 ![텍스트](https://github.com/kkkHoon/SST-Smart_Score_Tracker-/blob/master/Img/producer_diagram.png)  
- consumer thread diagram activity  
 ![텍스트](https://github.com/kkkHoon/SST-Smart_Score_Tracker-/blob/master/Img/consumer_diagram.png)  
