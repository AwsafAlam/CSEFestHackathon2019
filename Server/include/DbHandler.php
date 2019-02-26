<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class DbHandler {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    /* ------------- `users` table method ------------------ */

    /**
     * Creating new user
     * @param String $username User user_name
     * @param String $phonenumber User phone_number
     */
    public function createUser($username, $password) {
        require_once 'PassHash.php';
        $response = array();

        // First check if user already existed in db
        if (!$this->isUserExists($username)) {
            
            

            // Generating API key
            $api_key = $this->generateApiKey();

            // insert query
            $stmt = $this->conn->prepare("INSERT INTO users(username, password, api_key, status) values(?, ?, ?, 1)");
            $stmt->bind_param("sss",$username, $password,$api_key);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                return USER_CREATED_SUCCESSFULLY;
            } else {
                // Failed to create user
                return USER_CREATE_FAILED;
            }
        } else {
            // User with same username already existed in the db
            return USER_ALREADY_EXISTED;
        }

        return $response;
    }

    /**
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return boolean User login status success/fail
     */
    public function checkLogin($username, $password) {
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE username = ? and password= ?");

        $stmt->bind_param("ss", $username, $password);

         $result=$stmt->execute();
         //$result=$stmt->num_of_rows;
         $cont=0;
         while($stmt->fetch())
         {
            $cont=1;
         }
         

        

       return $cont>0;
       
    }
    /**
     * Checking for duplicate user by email address
     * @param String $email email to check in db
     * @return boolean
     */
    private function isUserExists($email) {
        $stmt = $this->conn->prepare("SELECT * from users WHERE username = ?");
        $stmt->bind_param("s",$email);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getUserByEmail($email) {
        $stmt = $this->conn->prepare("SELECT name, email, api_key, status, created_at FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        if ($stmt->execute()) {
            // $user = $stmt->get_result()->fetch_assoc();
            $stmt->bind_result($name, $email, $api_key, $status, $created_at);
            $stmt->fetch();
            $user = array();
            $user["name"] = $name;
            $user["email"] = $email;
            $user["api_key"] = $api_key;
            $user["status"] = $status;
            $user["created_at"] = $created_at;
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching user api key
     * @param String $user_id user id primary key in user table
     */
    public function getApiKeyById($user_id) {
        $stmt = $this->conn->prepare("SELECT api_key FROM users WHERE id = ?");
        $stmt->bind_param("i", $user_id);
        if ($stmt->execute()) {
            // $api_key = $stmt->get_result()->fetch_assoc();
            // TODO
            $stmt->bind_result($api_key);
            $stmt->close();
            return $api_key;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching user id by api key
     * @param String $api_key user api key
     */
    public function getUserId($api_key) {
        $stmt = $this->conn->prepare("SELECT id FROM users WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        if ($stmt->execute()) {
            $stmt->bind_result($user_id);
            $stmt->fetch();
            // TODO
            // $user_id = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user_id;
        } else {
            return NULL;
        }
    }

    /**
     * Validating user api key
     * If the api key is there in db, it is a valid key
     * @param String $api_key user api key
     * @return boolean
     */
    public function isValidApiKey($api_key) {
        $stmt = $this->conn->prepare("SELECT id from users WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Generating random Unique MD5 String for user Api key
     */
    private function generateApiKey() {
        return md5(uniqid(rand(), true));
    }

    /* ------------- `tasks` table method ------------------ */

    /**
     * Creating new task
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function createIncident($username,$image,$imgmp,$age, $gender, $date,$location, $location_details, $contact,$description ) {
        $response = array();
        $id=0; 
        $api_key = $this->generateApiKey();
       $path="$image.png";
        while(file_exists($path))
           {
              $image=$image+"1";
              $path="$image.png";
           } 
        $actualpath = "http://198.211.96.87/helpandclick/v1/$image.png";
        $stmt = $this->conn->prepare("INSERT INTO incidents(username,bigimage,age,gender,date,location,location_details,contact,description,api_key) VALUES(?,?,?,?,?,?,?,?,?,?)");
        
        $stmt->bind_param("ssssssssss", $username, $actualpath,$age,$gender, $date, $location, $location_details,$contact,$description,$api_key);
        $result = $stmt->execute();
        
        
        
        $path="$image.png";
        //echo $imgmp;  
        if($result){
         
 file_put_contents($path,base64_decode($imgmp));
 
 }
         
        $stmt->close();





     
        if ($result) {
            // task row created
            // now assign the task to user
            //$new_task_id = $this->conn->insert_id;
            //$res = $this->createUserTask($user_id, $new_task_id);
            //if ($res) {
                // task created successfully
                return 1;
            } //else {
                // task failed to create
                //return 0;
            //}
        //} 
      else {
            // task failed to create
            return 0;
        }
    }


 public function createnotice($username,$image,$imgmp,$name,$age, $gender, $date,$location, $occupation, $appearance,$contact,$addition ) {
        $response = array();
        $api_key = $this->generateApiKey();
        $path="$image.png";
        while(file_exists($path))
           {
              $image=$image+"1";
              $path="$image.png";
              
           } 
        $actualpath = "http://198.211.96.87/helpandclick/v1/$image.png";
        $stmt = $this->conn->prepare("INSERT INTO notices(username,bigimage,name,age,gender,date,location,occupation,appearance,contact,addition,api_key) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        $stmt->bind_param("ssssssssssss", $username, $actualpath,$name,$age,$gender, $date, $location, $occupation, $appearance,$contact,$addition,$api_key);
        $result = $stmt->execute();
        
           $path="$image.png";
         if($result){
 file_put_contents($path,base64_decode($imgmp));
 //echo "Successfully Uploaded";
 }
         
        $stmt->close();
       





     
        if ($result) {
            // task row created
            // now assign the task to user
            //$new_task_id = $this->conn->insert_id;
            //$res = $this->createUserTask($user_id, $new_task_id);
            //if ($res) {
                // task created successfully
                return 1;
            } //else {
                // task failed to create
                //return 0;
            //}
        //} 
      else {
            // task failed to create
            return 0;
        }
    }
public function createIncidentComments($username,$post_id,$comment) {
        $response = array();
        $api_key = $this->generateApiKey();
        $stmt = $this->conn->prepare("INSERT INTO comments_posts(username,post_id,comment,api_key) VALUES(?,?,?,?)");
        
        $stmt->bind_param("ssss", $username,$post_id,$comment,$api_key);
        $result = $stmt->execute();
        $stmt->close();





     
        if ($result) {
            // task row created
            // now assign the task to user
            //$new_task_id = $this->conn->insert_id;
            //$res = $this->createUserTask($user_id, $new_task_id);
            //if ($res) {
                // task created successfully
                return 1;
            } //else {
                // task failed to create
                //return 0;
            //}
        //} 
      else {
            // task failed to create
            return 0;
        }
    }
public function createNoticeComments($username,$post_id,$comment) {
        $response = array();
        $api_key = $this->generateApiKey();
        $stmt = $this->conn->prepare("INSERT INTO comments_notices(username,notice_id,comment,api_key) VALUES(?,?,?,?)");
        
        $stmt->bind_param("ssss", $username,$post_id,$comment,$api_key);
        $result = $stmt->execute();
        $stmt->close();





     
        if ($result) {
            // task row created
            // now assign the task to user
            //$new_task_id = $this->conn->insert_id;
            //$res = $this->createUserTask($user_id, $new_task_id);
            //if ($res) {
                // task created successfully
                return 1;
            } //else {
                // task failed to create
                //return 0;
            //}
        //} 
      else {
            // task failed to create
            return 0;
        }
    }

    /**
     * Fetching single task
     * @param String $task_id id of the task
     */
    public function getTask($task_id, $user_id) {
        $stmt = $this->conn->prepare("SELECT t.id, t.task, t.status, t.created_at from tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        if ($stmt->execute()) {
            $res = array();
            $stmt->bind_result($id, $task, $status, $created_at);
            // TODO
            // $task = $stmt->get_result()->fetch_assoc();
            $stmt->fetch();
            $res["id"] = $id;
            $res["task"] = $task;
            $res["status"] = $status;
            $res["created_at"] = $created_at;
            $stmt->close();
            return $res;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getAllUserPosts($user_id) {
        $stmt = $this->conn->prepare("SELECT id, bigimage,age,gender,location FROM incidents WHERE username = ? order by id desc");
        
       mysqli_stmt_bind_param($stmt,"s", $user_id);
        
        $stmt->execute();
        
        $stmt->bind_result($id, $bigimage,$age,$gender,$location);
        //echo ($user_id); 
       $tasks = array();
       while($stmt->fetch()) {
           
           $tmp = array();
           $tmp["id"] = $id;
           //echo ($id); 
           
           $tmp["bigimage"] = $bigimage;
           $tmp["age"] = $age;
           $tmp["gender"] = $gender;
           $tmp["location"] = $location;
           array_push($tasks, $tmp);
       }
        
        $stmt->close();
        return $tasks;
    }

   public function gettest($user_id) {
        $stmt = $this->conn->prepare("SELECT a from td");
        
       
        
        $stmt->execute();
        
        $stmt->bind_result($id);
        //echo ($user_id); 
       $tasks = array();
       while($stmt->fetch()) {
           
           $tmp = array();
           $tmp["id"] = $id;
           //echo ($id); 
           
           
           array_push($tasks, $tmp);
       }
        
        $stmt->close();
        return $tasks;
    }
     public function getAllUserPostsfull($id) {
        $stmt = $this->conn->prepare("SELECT * FROM incidents where id =? order by id desc");
        
        $stmt->bind_param('i', $id);
        
        $stmt->execute();
        $stmt->bind_result($ids,$username,$bigimage,$age,$gender,$date,$location,$location_details,$contact,$description,$created_at,$api_key);
        //$tasks = $stmt->get_result();
        $tasks = array();
       while($stmt->fetch()) {
           $tmp = array();
           //echo ($id);
           //$tmp["id"] = $ids;
           $tmp["location"] = $location;
           $tmp["bigimage"] = $bigimage;
           $tmp["age"] = $age;
           $tmp["gender"] = $gender;
           $tmp["date of incident"] = $date;
           $tmp["location details"]=$location_details;
           $tmp["contact"] = $contact;
           $tmp["description"] = $description;
           array_push($tasks, $tmp);
       }
       
        $stmt->close();
        return $tasks;
    }
   public function getAllPosts($user_id) {
        $stmt = $this->conn->prepare("SELECT id, bigimage,age,gender,date,location FROM incidents order by id desc ");
        //echo $user_id;
        //$stmt->bind_param("s", $user_id);
        $stmt->execute();

       $stmt->bind_result($id, $bigimage,$age,$gender,$date,$location);
       $tasks = array();
       while($stmt->fetch()) {
           $tmp = array();
           $tmp["id"] = $id;
           $tmp["location"] = $location;
           $tmp["bigimage"] = $bigimage;
           $tmp["age"] = $age;
           $tmp["date of incident"] = $date;
           $tmp["gender"] = $gender;
           array_push($tasks, $tmp);
       }
       $stmt->close();
       
             
       return $tasks;

//
//        $tasks = $stmt->get_result();
//        echo "Hello";
//        $stmt->close();
//        return $tasks;
    }
    public function getAllUserNots($user_id) {
        $stmt = $this->conn->prepare("SELECT id,bigimage,name,age,gender FROM notices where username =? order by id desc");
        
        mysqli_stmt_bind_param($stmt,"s", $user_id);
        $stmt->execute();
        //$tasks = $stmt->get_result();
        $stmt->bind_result($id, $bigimage,$name,$age,$gender);
        $tasks = array();
        while($stmt->fetch()) {
           $tmp = array();
           $tmp["id"] = $id;
           $tmp["name"] = $name;
           $tmp["bigimage"] = $bigimage;
           $tmp["age"] = $age;
           $tmp["gender"] = $gender;
           array_push($tasks, $tmp);
       }
        $stmt->close();
        return $tasks;
    }
public function getAllUserNotsfull($id) {
        $stmt = $this->conn->prepare("SELECT * FROM notices where id =? order by id desc");
        
        $stmt->bind_param("s", $id);
        $stmt->execute();
        $stmt->bind_result($ids,$username,$bigimage,$name,$age,$gender,$date,$location,$occupation,$appearance,$contact,$description,$created_at,$api_key);
        //$tasks = $stmt->get_result();
        $tasks = array();
        while($stmt->fetch()) {
           $tmp = array();
           //echo ($id);
           $tmp["name"] = $name;
           $tmp["username"] = $username;
           $tmp["location"] = $location;
           $tmp["bigimage"] = $bigimage;
           $tmp["age"] = $age;
           $tmp["gender"] = $gender;
           $tmp["date of incident"] = $date;
           $tmp["occupation"]=$occupation;
           $tmp["appearance"]=$appearance;
           $tmp["contact"] = $contact;
           $tmp["description"] = $description;
           array_push($tasks, $tmp);
       }

        //$tasks = $stmt->get_result();
        $stmt->close();
        return $tasks;
    }
public function getAllNotices($user_id) {
        $stmt = $this->conn->prepare("SELECT id,bigimage,name,age,gender,location FROM notices order by id desc");
        
        //$stmt->bind_param("s", $user_id);
        $stmt->execute();
        $stmt->bind_result($id, $bigimage,$name,$age,$gender,$location);
       $tasks = array();
       while($stmt->fetch()) {
           $tmp = array();
           $tmp["id"] = $id;
           $tmp["name"] = $name;
           $tmp["bigimage"] = $bigimage;
           $tmp["age"] = $age;
           $tmp["gender"] = $gender;
           $tmp["location"] = $location;
           array_push($tasks, $tmp);
       }
        
        $stmt->close();
        return $tasks;
    }
public function getAllIncidentComments($id) {
        $stmt = $this->conn->prepare("SELECT username,comment FROM comments_posts where post_id =? order by id asc");
        
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->bind_result($username, $comment);
        $tasks = array();
        while($stmt->fetch()) {
           $tmp = array();
           $tmp["username"] = $username;
           $tmp["comment"] = $comment;
          // $tmp["bigimage"] = $bigimage;
         //  $tmp["age"] = $age;
          // $tmp["gender"] = $gender;
           array_push($tasks, $tmp);
       }
       // $tasks = $stmt->get_result();
        $stmt->close();
        return $tasks;
    }
public function getAllNoticeComments($id) {
        $stmt = $this->conn->prepare("SELECT username,comment FROM comments_notices where notice_id= ? order by id asc");
        
        $stmt->bind_param("i", $id);
        $stmt->execute();
        //$tasks = $stmt->get_result();
        $stmt->bind_result($username, $comment);
        $tasks = array();
        while($stmt->fetch()) {
           $tmp = array();
           $tmp["username"] = $username;
           $tmp["comment"] = $comment;
          // $tmp["bigimage"] = $bigimage;
         //  $tmp["age"] = $age;
          // $tmp["gender"] = $gender;
           array_push($tasks, $tmp);
       }
        $stmt->close();
        return $tasks;
    }



    /**
     * Updating task
     * @param String $task_id id of the task
     * @param String $task task text
     * @param String $status task status
     */
    public function updateTask($user_id, $task_id, $task, $status) {
        $stmt = $this->conn->prepare("UPDATE tasks t, user_tasks ut set t.task = ?, t.status = ? WHERE t.id = ? AND t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("siii", $task, $status, $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

    /**
     * Deleting a task
     * @param String $task_id id of the task to delete
     */
    public function deletePost($task_id) {
        $stmt = $this->conn->prepare("DELETE FROM incidents where id = ?");
        $stmt->bind_param("s", $task_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        $stmt = $this->conn->prepare("DELETE  FROM comments_posts where post_id = ?");
        $stmt->bind_param("s", $task_id);
        $stmt->execute();

        return $num_affected_rows > 0;
    }
   public function deleteNotice($task_id) {
        $stmt = $this->conn->prepare("DELETE FROM notices where id = ?");
        $stmt->bind_param("s", $task_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
       $stmt = $this->conn->prepare("DELETE  FROM comments_notices where notice_id = ?");
        $stmt->bind_param("s", $task_id);
        $stmt->execute();
        return $num_affected_rows > 0;
    }

    /* ------------- `user_tasks` table method ------------------ */

    /**
     * Function to assign a task to user
     * @param String $user_id id of the user
     * @param String $task_id id of the task
     */
    public function createUserTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("INSERT INTO user_tasks(user_id, task_id) values(?, ?)");
        $stmt->bind_param("ii", $user_id, $task_id);
        $result = $stmt->execute();

        if (false === $result) {
            die('execute() failed: ' . htmlspecialchars($stmt->error));
        }
        $stmt->close();
        return $result;
    }

}

?>
