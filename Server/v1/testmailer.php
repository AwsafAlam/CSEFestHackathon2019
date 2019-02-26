<?php
require ('mailer.php');
//   $message_email=$_POST[\"email\"];
//   $message_subject=$_POST[\"subject\"];
//   $message_body=$_POST[\"body\"];
  
$message_body = "<head>
  <meta name=\"viewport\" content=\"width=device-width\">
  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">
  <title>BUET Airlines</title>

  <style type=\"text/css\" data-immutable=\"true\">
    /* -------------------------------------
          FONTS
      ------------------------------------- */
      @import url(\"https://fast.fonts.net/t/1.css?apiType=css&projectid=43d8124e-fdde-4bce-b6cb-c5496474db5a\");
      @font-face{
      font-family:\"MetroNova_n2\";
      src:url(\"http://chris-armstrong.com/fonts/c282ed6d-f289-4246-b9e9-ca23086465cb.eot?#iefix\") format(\"eot\")
      }
      @font-face{
      font-family:\"MetroNova\";
      src:url(\"http://chris-armstrong.com/fonts/c282ed6d-f289-4246-b9e9-ca23086465cb.eot?#iefix\");
      src:url(\"http://chris-armstrong.com/fonts/c282ed6d-f289-4246-b9e9-ca23086465cb.eot?#iefix\") format(\"eot\"),url(\"http://chris-armstrong.com/fonts/89af966e-d3e3-4c30-ac08-90d105427594.woff\") format(\"woff\"),url(\"http://chris-armstrong.com/fonts/5f070be2-fe7d-455e-a15b-6ce5ef5cdcae.ttf\") format(\"truetype\"),url(\"http://chris-armstrong.com/fonts/7c6fb5d5-d07d-4d88-8df5-61a0947d86c9.svg#7c6fb5d5-d07d-4d88-8df5-61a0947d86c9\") format(\"svg\");
      font-weight: 200;
      font-style: normal;
      }
      @font-face{
      font-family:\"MetroNova_n3\";
      src:url(\"http://chris-armstrong.com/fonts/c8c9aba5-f3a6-4e34-a524-70823be702f5.eot?#iefix\") format(\"eot\")
      }
      @font-face{
      font-family:\"MetroNova\";
      src:url(\"http://chris-armstrong.com/fonts/c8c9aba5-f3a6-4e34-a524-70823be702f5.eot?#iefix\");
      src:url(\"http://chris-armstrong.com/fonts/c8c9aba5-f3a6-4e34-a524-70823be702f5.eot?#iefix\") format(\"eot\"),url(\"http://chris-armstrong.com/fonts/969ff0b9-0f7c-4b65-a9c2-839849ffb133.woff\") format(\"woff\"),url(\"http://chris-armstrong.com/fonts/d7f3eee0-3187-4c2d-87e9-00a566a44133.ttf\") format(\"truetype\"),url(\"http://chris-armstrong.com/fonts/7eb485c4-0102-47f8-be76-8b02acb3f853.svg#7eb485c4-0102-47f8-be76-8b02acb3f853\") format(\"svg\");
      font-weight: 300;
      font-style: normal;
      }
  </style>

  <style type=\"text/css\">
    a,img{border:none}.btn-primary,a{text-decoration:none}.btn,.btn-primary{letter-spacing:.85px}.btn-primary,.reply,.ta-c{text-align:center}*{margin:0;padding:0;font-family:\"Helvetica Neue\",Helvetica,Helvetica,Arial,sans-serif;font-size:100%;line-height:25px}.btn-primary,h1,h2,h3{font-family:MetroNova,\"Helvetica Neue\",Helvetica,Arial,\"Lucida Grande\",sans-serif;color:#212121}img{max-width:100%;display:block}body{-webkit-font-smoothing:antialiased;-webkit-text-size-adjust:none;width:100%!important;height:100%;color:#212121}a{color:#9BA826}.btn-primary{text-transform:uppercase;background-color:none;border:2px solid #cad568;padding:0 20px;line-height:42px;margin:auto;cursor:pointer;display:inline-block;border-radius:50px;white-space:nowrap;font-size:12px;font-weight:600}h1,h2,h3{margin:0;line-height:1.2;font-weight:200}h2{font-size:42px;line-height:50px;margin-top:0;padding-bottom:25px}h3{font-size:25px;line-height:37px}h4,ol,p,ul{font-size:16px}h4{line-height:25px;font-weight:700}ol,p,ul{margin:0 auto 20px;font-weight:400;color:#777;-moz-font-feature-settings:'ss01';-webkit-font-feature-settings:'ss01';font-feature-settings:'ss01'}.body h1,.btn{color:#212121}ol li,ul li{margin-left:5px;list-style-position:inside}.container{margin:0 auto;max-width:485px}.body,.footer,.header{padding:50px}.header{padding-bottom:0;border-top:solid 3px #212121}.header h1{margin:0}.body h1,.body p{margin:0 0 20px}.footer{background:#f2f2f2}.body h1{font-size:42px;line-height:50px}.btn{border:2px solid #CFCF6D;display:inline-block;padding:3px;font-weight:600;font-size:12px;line-height:15px;text-transform:uppercase}.footer img{margin-bottom:15px}.board-activity,.board-header,hr{margin-bottom:30px}.footer p{font-size:14px;color:#bbb}hr{border:none;border-bottom:1px solid #F2F2F2}p.small{font-size:14px;line-height:20px}p.x-small{font-size:12px;opacity:.6}p.x-small a{color:#777;text-decoration:underline}.board-activity td,.board-header td{padding:10px}.board-activity .avatar,.board-activity .board,.board-activity .line,.board-header .avatar,.board-header .board,.board-header .line{width:50px;padding:0}.board-activity .avatar img,.board-activity .board img,.board-activity .line img,.board-header .avatar img,.board-header .board img,.board-header .line img{margin-right:0;margin-bottom:0}.board-activity img,.board-header img{margin-right:20px;display:inline-block;vertical-align:top;max-width:113px;margin-bottom:20px}.board-activity .avatar,.board-header .avatar{border-radius:100%;width:50px;height:50px}.board-activity .line,.board-header .line{background:url(https://niice.co/assets/emails/bg-line.jpg) center 0 repeat-y}.board-activity{margin-bottom:50px}.board-activity tr:last-child .line{background:0 0}.reply{font-size:14px;opacity:.6}.board-invite{margin-bottom:30px}.board-invite hr{margin-bottom:0;border-bottom:2px dashed #777}.board-invite table{width:100%;table-layout:fixed}.board-invite table .board-invite__board{width:150px}.board-invite td{vertical-align:middle}.board-invite p{font-weight:700;margin-bottom:0;color:#212121}.board-invite img{display:inline}.board-invite .avatar{text-align:center;width:74px}.board-invite .avatar img{margin-top:20px}.board-invite .avatar p{text-overflow:ellipsis;overflow:hidden;width:72px;white-space:nowrap;font-size:14px;line-height:20px}
  </style>
</head>

<body>

  <div class=\"header\">
    <div class=\"container\">
      <h1 style=\"font-size: 350%; color: #880e4f;\" >BUET Airlines</h1>
    </div>
  </div>

  <div class=\"body\">
    <div class=\"container\">
      <h1>Thanks for your payment!</h1>

      <p>We have attached your receipt to this email.</p>

      <p>
        <strong>Flight ID:</strong> <br>
      </p>

      <p>(If you have any questions or feedback, just <a href=\"mailto:chris@niice.co\" target=\"_blank\">reply to this email</a>)</p>

    </div>
  </div>

  <div class=\"footer\">
    <div class=\"container\">
      <p>This email was sent by BUET Airlines Ltd.</p>
      <p>West Palashi Campus<br>
            ECE Building, Azimpur Road<br>
            Dhaka - 1205<br>
            </p>
    </div>
  </div>


</body>";
$message_subject = "Greetings From kolpoBD";
$message_email = "awsafalam@gmail.com"; // receiver email address
$message_title = "KolpoBD";

// echo "Hello";

$mailSender = new MailSender($message_email, $message_subject, $message_title, $message_body);
$mailSender->requestMailSend();

// $message_subject = \"Greetings From BuetAirlines\";
// $EMAIL_ID = $Email;
// $message_title = \"BUET Airlines\";

// $mailSender = new MailSender($EMAIL_ID, $message_subject, $message_title, $message_body);

// $mailSender->requestMailSend();


?>