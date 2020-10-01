<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <title>Booking on Renaissance</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="email-styles.css">
</head>
<body>
    <div class="message-container">
        <div class="head-row">
            <p><b>Renaissance</b></p>
        </div>
        <div class="body-row">
            <p>Dear ${username}, thanks for booking</p>
            <p>${content}</p>
        </div>
        <div class="bottom-row">
            <p>Best Regards</p>
            <p>Renaissance Team</p>
        </div>
    </div>
</body>
</html>
