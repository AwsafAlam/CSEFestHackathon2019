<?php
// Import PHPMailer classes into the global namespace
// These must be at the top of your script, not inside a function
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
class MailSender{
    private $email_recipient;
    private $message_subject;
    private $message_body;
    private $message_title;

    public function __construct( $recipient, $subject, $title, $body) {
        $this->email_recipient = $recipient;
        $this->message_subject = $subject;
        $this->message_title = $title;
        $this->message_body = $body;
    }
    function requestMailSend(){
        //Load composer's autoloader
        require 'PHPMailer/vendor/autoload.php';
        $message_date = date("Y-m-d");
        $message_time = date("H:i:s a");
        $mail = new PHPMailer(true);                              // Passing `true` enables exceptions
        try {
            //Server settings
            $mail->SMTPOptions = array(
                'ssl' => array(
                    'verify_peer' => false,
                    'verify_peer_name' => false,
                    'allow_self_signed' => true
                )
            );
            $mail->SMTPDebug = 3;                                 // Enable verbose debug output
            $mail->isSMTP();                                      // Set mailer to use SMTP
            $mail->Host = 'mail.kolpobd.com';  // Specify main and backup SMTP servers
            $mail->SMTPAuth = true;                               // Enable SMTP authentication
            $mail->Username = 'info@kolpobd.com';                 // SMTP username (sender mail)
            $mail->Password = 'kolpoBD@15';                           // SMTP password (sender password)
            $mail->SMTPSecure = 'tls';                            // Enable TLS encryption, `ssl` also accepted
            $mail->Port = 26;                                    // TCP port to connect to
            //Recipients
            $mail->setFrom('info@kolpobd.com', 'KolpoBD');
            $mail->addAddress($this->email_recipient);     // Add a recipient
            //Content
            $mail->isHTML(true);                                  // Set email format to HTML
            $mail->Subject = $this->message_subject;
            $mail->Body    = $this->message_body ;
            $mail->send();
//            echo 'Message has been sent';
        } catch (Exception $e) {
            echo 'Message could not be sent.';
            echo 'Mailer Error: ' . $mail->ErrorInfo;
        }
    }
}
?>