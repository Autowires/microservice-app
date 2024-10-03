<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .container {
            max-width: 500px; /* Set max width to make the form more compact */
            margin-top: 50px; /* Top margin for centering */
        }

        .form-group {
            margin-bottom: 1rem;
        }

        .otp-button {
            margin-left: 5px; /* Spacing between email field and button */
        }

        .verified {
            background-color: #28a745; /* Green color */
            color: white;
        }
    </style>
    <script>
        let otpVerified = false;

        async function verifyEmail() {
            const email = document.getElementById("email").value;

            const response = await fetch(`/register/verify-email?email=` + encodeURIComponent(email), {
                method: "POST"
            });

            if (response.ok) {
                // Show OTP modal
                $('#otpModal').modal('show');
            } else {
                const error = await response.text();
                alert("Error: " + error);
            }
        }

        async function verifyOtp() {
            const email = document.getElementById("email").value;
            const otp = document.getElementById("otp").value;

            const response = await fetch('/register/verify-email', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, otp })
            });

            const result = await response.text();
            alert(result);

            if (response.ok) {
                otpVerified = true;
                const verifyButton = document.querySelector(".otp-button");
                verifyButton.classList.add("verified"); // Add green class
                verifyButton.innerText = "Verified"; // Change button text
                verifyButton.disabled = true; // Disable the button
                document.getElementById("registerButton").disabled = !otpVerified;
                $('#otpModal').modal('hide');
            }
        }
    </script>
</head>

<body>
    <div class="container">
        <h2 class="text-center">Register</h2>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>

        <form action="/register" method="post">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" class="form-control" name="username" required>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <div class="input-group">
                    <input type="email" class="form-control" name="email" id="email" required>
                    <div class="input-group-append">
                        <button type="button" class="btn btn-secondary otp-button" onclick="verifyEmail()">Verify</button>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label for="contactNo">Contact Number</label>
                <input type="tel" class="form-control" name="contactNo" required maxlength="10">            </div>
             <div class="form-group">
                <label for="age">Age</label>
                <input type="age" class="form-control" name="age" required>
            </div>
             <div class="form-group">
                <label for="city">City</label>
                <input type="city" class="form-control" name="city" required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" class="form-control" name="password" required>
            </div>

            <button type="submit" class="btn btn-primary" id="registerButton" disabled>Register</button>
        </form>
    </div>

    <!-- Modal for OTP Verification -->
    <div class="modal fade" id="otpModal" tabindex="-1" role="dialog" aria-labelledby="otpModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="otpModalLabel">Verify OTP</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="$('#otpModal').modal('hide')">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="otp">Enter OTP</label>
                        <input type="text" id="otp" class="form-control" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" onclick="verifyOtp()">Verify OTP</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="Close">Cancel</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>

</html>
