package jbcdConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;


public class HangMan {
	HangMan(){
		createWordList();
	}
	static HangMan mainObj = new HangMan();
	 Map<String, String> wordCategoryMap = new HashMap<>();
	int lives = 5;
		 public static void main(String[] args) {
			 System.out.println("Welcome to Hangman Game \n");
			 System.out.println("1.Admin Login");
			 System.out.println("2.User Login");
			 System.out.println("3.Create new account");
			 System.out.println("Enter your option");
			
			try {
					 int userChoose = mainObj.inputValue.nextInt();
					 mainObj.inputValue.nextLine();
					 switch (userChoose) {
					 	case 1:mainObj.adminLoginInput();
					 			break;
					 	case 2: mainObj.userLoginInput();
					 			break;
					 
						case 3: mainObj.newUserInput();
								break;
						default:
							System.out.println("Enter a valid input");
						}
				} catch (Exception e) {
					System.out.println("Enter a vaild input");
				}
				

		 }
		Scanner inputValue = new Scanner(System.in);
		
		public void GuessInputValue() {
			 String guessWordForUser = getRandomWord();
			 String guessedWord = "";
			 String[] wordList = guessWordForUser.split("");
			 System.out.println("	Lives : " + mainObj.lives+"\n");
			 System.out.println("__ ".repeat(guessWordForUser.length()));
			
			 while(mainObj.lives > 0) {
				 System.out.println("Enter the value ");
				 String userValue = inputValue.next().charAt(0)+"";
				 guessedWord = mainObj.checkValue(userValue,wordList,guessedWord);
				 	
				 if(mainObj.lives == 0) {
					 System.out.println("\n\nYou lose the game.");
					 mainObj.userObj.wordResultToDB(guessWordForUser,false);
					 break;
				 }
				 
				 if(guessedWord.length() == guessWordForUser.length()) {
					 System.out.println("\n\nYou guess the word.");
					 mainObj.userObj.wordResultToDB(guessWordForUser,true);
					 break;
				 }
				 System.out.println();
			 }
		}
		 
		public boolean isCorrectChar(String userInput, String[] valuesList) {
			 boolean isCorrect = false;
			 for(String charater:valuesList) {
				 if(userInput.equalsIgnoreCase(charater)) {
					 isCorrect = true;
				 }
			 }
			 return isCorrect;
		 }
		 
		 
		public String checkValue(String inputCharacter,String[] valuesList,String guessedWord){
			boolean isVaildInput = mainObj.isCorrectChar(inputCharacter, valuesList);
				if(!isVaildInput) {
					mainObj.lives--;
					if(mainObj.lives == 0) {
						return "";
					}
					System.out.println("Invalid input. Please enter a valid letter.\n\n");
					System.out.println("Lives remaining: " + mainObj.lives);
					}
				for (String character : valuesList) {
					if(inputCharacter.equalsIgnoreCase(character)||(guessedWord.contains(character))) {
						System.out.print(character+" ");
						if(inputCharacter.equalsIgnoreCase(character)) {
							guessedWord+=character;
						}
					}
					else {
						System.out.print("__ ");
					}
				}
				
				return guessedWord;
			}
		
		// create user obj
		user userObj = new user();
		Admin adminObj = new Admin();
		// New User InputValue
		public void newUserInput() {
			System.out.println("Enter Your username ");
			String userName = inputValue.nextLine();
			System.out.println("Enter Your Password ");
			String userPassword = inputValue.next();
			boolean isCreated = userObj.createNewUser(userName,userPassword);
			if(isCreated) {
				System.out.println("Account create successfully...");
				userObj.getUserDetailsFromDB(userName,userPassword);
			}
			else {
				System.out.println("\nUser name already exists\n");
				inputValue.nextLine();
				newUserInput();
			}
			
		}
		// User Login
		public void userLoginInput() {
			System.out.println("Enter Your username ");
			String userName = inputValue.nextLine();
			System.out.println("Enter Your Password ");
			String userPassword = inputValue.next();
			userObj.getUserDetailsFromDB(userName,userPassword);
		}
		
		// admin login
		public void adminLoginInput() {
			System.out.println("Enter Your username ");
			String userName = inputValue.nextLine();
			System.out.println("Enter Your Password ");
			String userPassword = inputValue.next();
			adminObj.adminCheck(userName,userPassword);
		}
		
		String getRandomWord() {
			String[] words = wordCategoryMap.keySet().toArray(new String[0]);
			int randomIndex = new Random().nextInt(words.length);
			;
			System.out.print(wordCategoryMap.get(words[randomIndex]));
			return words[randomIndex];
		}

		

		void createWordList() {
			String sql = "select W_Name,C_Name from WordList join Category on Category.C_id = WordList.C_id;";
			try {
				Connection connection = DBConnect.getDBConnection().getConnectLink();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery();
				while(resultSet.next()) {
					String wordName = resultSet.getString("W_Name");
					String categoryName = resultSet.getString("C_Name");
					wordCategoryMap.put(wordName, categoryName);
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("WordList is blocked");
			}

		}
		
		void playAgain() {
			System.out.println("Do you want to play again ? yes(1) or no(2)");
			int isPlay = inputValue.nextInt();
			switch (isPlay) {
			case 1: 
				GuessInputValue();
				break;
			default:
				System.out.println("Come again ");
			}
		}
}

class user{
	String userName;
	int userID;
	Connection connection;
	user(){
			try {
				 connection = DBConnect.getDBConnection().getConnectLink();;
	
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	
	 void accountMenuList(){
	 		System.out.print("\n\n       -------------------------------------- \n\n");
			System.out.format("     | %-38s |\n\n", "      1. Play Game");
    		System.out.format("     | %-38s |\n\n", "      2. View a history ");
    		System.out.format("     | %-38s |\n\n", "      3. Leader board ");
    		System.out.format("     | %-38s |\n\n", "      4. Logout");
			System.out.print("       -------------------------------------- \n\n");
			
			System.out.print("Enter your choice (1-4): ");
			int userNumber = HangMan.mainObj.inputValue.nextInt();
			switch (userNumber) {
			case 1:
				HangMan.mainObj.GuessInputValue();
				accountMenuList();
				break;
			case 2:
				userLists();
				accountMenuList();
				break;
			case 3:
				listTopUserBoard();
				accountMenuList();
				break;
			case 4:
				System.out.println("Thank you");
				break;
			default:
				System.out.println("Enter a valid input \n\n");
				accountMenuList();
				break;
			}
	 }
	
	boolean createNewUser(String username, String password) {
		boolean isValidUser = userAlreadyExists(username);
//		System.out.println(isValidUser);
		if(!isValidUser) {
			return false;
		}
        String sql = "INSERT INTO Users (Username, Password) VALUES (?, ?)";
		try  {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected);
            return rowsAffected > 0;
		}catch (Exception e) {
			System.out.println("Error");
			return false;
		}
		
	}
	
	
	boolean userAlreadyExists(String userName) {
		String sql = "SELECT * from Users";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				if(userName.equals(resultSet.getString("username"))) {
					return false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return true;
	}
	
	// Hang man obj
	
	void getUserDetailsFromDB(String username, String password) {
		 // User login is valid or not
		String sql = "SELECT * FROM Users WHERE Username = ?";
		try {
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            // User name is valid
            if (!resultSet.next()) {
            	HangMan.mainObj.inputValue.nextLine();
                System.out.println("Invalid User name. Please Enter a valid\n\n");
                HangMan.mainObj.userLoginInput();
               
            } else {	
            	int userId = resultSet.getInt("UserID");
	            String DBUsername = resultSet.getString("username");
	            String DBPassword = resultSet.getString("password");
	            // password name is valid
	            if(DBPassword.equalsIgnoreCase(password)) {
	            	this.userName = username; 
	            	this.userID = userId;
	            	System.out.println("Welcome "+username);
	            	accountMenuList();
	            }else {
	            	System.out.println("Incorrect password enter a valid password.");
	            	HangMan.mainObj.inputValue.nextLine();
	            	HangMan.mainObj.userLoginInput();
	            }
            }
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error acquire");
		}
	}
	
	// set user word to db
	void wordResultToDB(String GuessWord,boolean isGuessed) {
		String sql = "INSERT INTO Games (UserID, WordToGuess,isGuessed) VALUES (?, ?, ?)";
		try {
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, this.userID);
			preparedStatement.setString(2,GuessWord);
			byte wordGuessed = (byte) (isGuessed?1:0);
			preparedStatement.setByte(3,wordGuessed);
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error to db set word");
		}
		
	}
	
	void userLists() {
		String sql = "SELECT * from Games where UserID = ? ";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, this.userID);
			int totalRowCount = 0;
	        ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				if(totalRowCount == 0) {
					System.out.println("--------------------------------------------");
					System.out.printf("\n| %-15s | %-20s |\n", "Words", "isGuessed");
				}
				totalRowCount++;
				String guessedWord =  resultSet.getString("WordToGuess");
				int isGuessed = resultSet.getInt("isGuessed");
				boolean wordGuessed = (isGuessed == 1);
				System.out.printf("\n| %-15s | %-20b |\n", guessedWord, wordGuessed);				
			}
			System.out.println("--------------------------------------------");
			sql = "SELECT * from Games where UserID = ? and isGuessed = 1";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, this.userID);
			int guessedRow = 0;
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				guessedRow++;
			}
			System.out.println("\n\n");
			System.out.println("------------------------------------------------------------");
			System.out.printf("| %-15s | %-20s | %-15s |\n", "User Name", "Total Words", "Guessed Words");
			System.out.println("------------------------------------------------------------");

			System.out.printf("| %-15s | %-20d | %-15d |\n", this.userName, totalRowCount, guessedRow);
			System.out.println("------------------------------------------------------------");
			System.out.println("\n\n");
			HangMan.mainObj.playAgain();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("User history error");
		}
	}
	
	
	void listTopUserBoard() {
		
			String sql = "SELECT Users.UserName, COUNT(Games.UserID) as Count FROM Games JOIN Users ON Users.UserID = Games.UserID WHERE Games.isGuessed = 1 GROUP BY Users.UserID, Users.UserName ORDER BY Count DESC LIMIT 5";

            try {
            	PreparedStatement statement = connection.prepareStatement(sql);
            	ResultSet resultSet = statement.executeQuery();
            	System.out.printf("| %-15s  %-10s |","User Name","Total");
                    while (resultSet.next()) {
                        String UserName = resultSet.getString("UserName");
                        int totalWord = resultSet.getInt("Count");
                        System.out.printf("\n| %-15s  %-10d |",UserName,totalWord);
                        
                        System.out.println();
                    }
            }
            catch (Exception e) {
				System.out.println("Leader board");
			}
		
	}
	
}


class Admin {
	String userName = "Gopika";
	String password = "Go@123";
	Connection connection = DBConnect.getDBConnection().getConnectLink();
	  public void adminMenu() {
	        // Admin operations menu
		  
		  	System.out.print("\n\n       -------------------------------------- \n\n");
			System.out.format("     | %-38s |\n\n", "      1. Add New Word");
			System.out.format("     | %-38s |\n\n", "      2. View User List");
			System.out.format("     | %-38s |\n\n", "      3. Logout");
			System.out.print("\n\n       -------------------------------------- \n\n");
	       
			System.out.print("Enter your choice (1-3): ");
	        int adminChoice = HangMan.mainObj.inputValue.nextInt();
	        switch (adminChoice) {
	            case 1:
	                addNewWord();
	                adminMenu();
	                break;
	            case 2:
	            	viewAllUser();
	            	adminMenu();
	                break;
	            case 3:
	            	System.out.println("Thank you");
	            	break;
	            default:
	                System.out.println("Invalid option");
	                adminMenu();
	        }
	    }
	  
	  void adminCheck(String username, String password) {
		  if((username.equals(userName)) && (password.equals(this.password))) {
			  adminMenu();
		  }
		  else {
			  System.out.println("Invalid User Name and Password.\n");
			  HangMan.mainObj.inputValue.nextLine();
			  HangMan.mainObj.adminLoginInput();
		  }
	  }
	  
	  void addNewWord() {
		  chooseCategory();
		  insertWordtoDB();
	  }
	  
	  int count;
	  void chooseCategory() {
		   count = 0;
		  String sql = "select C_Name from Category";
		  try {
			  PreparedStatement statement = connection.prepareStatement(sql);
			  ResultSet resultSet = statement.executeQuery();
			  while(resultSet.next()) {
				  count++;
				  String categoryName = resultSet.getString("C_Name");
				  System.out.println(count +"." +categoryName);
			  }
			  System.out.print("Choose your Category (1-"+ count+"): ");
		  }
		  catch (Exception e) {
			System.out.println("Error in list the category");
		}
	  }
	  
	  void insertWordtoDB() {
		  int adminChoose = HangMan.mainObj.inputValue.nextInt();
		  if((adminChoose>0) && (adminChoose<= count)) {
			  System.out.println("Enter a word");
			  String wordString = HangMan.mainObj.inputValue.next();
			  try {
				  String sql = "Insert into WordList(W_Name, C_Id) values (?,?)";
				  PreparedStatement statement = connection.prepareStatement(sql);
				  statement.setString(1, wordString);
				  statement.setInt(2, adminChoose);
				  statement.executeUpdate();
				  System.out.println("\n"+wordString+" added Successfully.");
			} catch (Exception e) {
				System.out.println("Error in add new word");
			}
		  }
		  else {
			  System.out.println("Enter a valid inputs\n");
			  System.out.print("Choose your Category");
		  }
	  }
	  
	  void viewAllUser() {
		  String sql = "SELECT Users.UserName, COUNT(Games.UserID) AS TotalWordsPlayed, SUM(Games.isGuessed) AS CorrectlyGuessedWords FROM Games JOIN Users ON Users.UserID = Games.UserID GROUP BY Users.UserID, Users.UserName ORDER BY TotalWordsPlayed DESC LIMIT 5";
		  try {
		     PreparedStatement preparedStatement = connection.prepareStatement(sql);
		     ResultSet resultSet = preparedStatement.executeQuery();

		     	  System.out.println("-----------------------------------------------------------------------------");
				  System.out.printf("| %-20s | %-20s | %-30s |\n", "UserName", "TotalWordsPlayed", "CorrectlyGuessedWords");
		          System.out.println("-----------------------------------------------------------------------------");
		
		          while (resultSet.next()) {
		              String userName = resultSet.getString("UserName");
		              int totalWordsPlayed = resultSet.getInt("TotalWordsPlayed");
		              int correctlyGuessedWords = resultSet.getInt("CorrectlyGuessedWords");
		
		              System.out.printf("| %-20s | %-20d | %-30d |\n", userName, totalWordsPlayed, correctlyGuessedWords);
		          }
		
		          System.out.println("-----------------------------------------------------------------------------");  
			  }catch (Exception e) {
				System.out.println("Error in view all users");
			}
	  }
}


class DBConnect{
	private static DBConnect dataBaseConnect= new DBConnect();
	
	Connection connection;
	private DBConnect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/HangMan", "gopika", "1234");
		}
		catch (Exception e) {
			System.out.println("Data base error ");
		}
	}
	
	static DBConnect getDBConnection() {
		return dataBaseConnect;
	}
	
	Connection getConnectLink() {
		return connection;
	}
	
}