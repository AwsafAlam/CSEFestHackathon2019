

<?php

/**
 * Handling database connection
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class DbConnect {

    private $conn;

    function __construct() {        
    }

    /**
     * Establishing database connection
     * @return database connection handler
     */
    function connect() {
        include_once dirname(__FILE__) . '/Config.php';

        // Connecting to mysql database
        $this->conn = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
        //mysqli_query($this->conn,'SET CHARACTER SET utf8');
        //mysqli_query($this->conn,"SET SESSION collation_connection ='utf8_general_ci'");
        //mysqli_query($this->conn,'SET character_set_results=utf8');
        //$this->conn->query(‘SET CHARACTER SET utf8’);
        //$this->conn->query(“SET SESSION collation_connection =’utf8_general_ci'”);
        // Check for database connection error
        if (mysqli_connect_errno()) {
            echo "Failed to connect to MySQL: " . mysqli_connect_error();
        }

        // returing connection resource
        return $this->conn;
    }

}

?>
