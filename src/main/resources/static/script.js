document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('register-form');
    const loginForm = document.getElementById('login-form');
    const getUsersButton = document.getElementById('get-users-button');
    const registerMessage = document.getElementById('register-message');
    const loginMessage = document.getElementById('login-message');
    const apiMessage = document.getElementById('api-message');
    const usersList = document.getElementById('users-list');

    let jwtToken = null;

    // Helper function to display messages
    function displayMessage(element, message, isSuccess) {
        element.textContent = message;
        element.className = isSuccess ? 'success' : 'error';
    }

    // Register user
    registerForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const username = document.getElementById('register-username').value;
        const password = document.getElementById('register-password').value;

        try {
            const response = await fetch('/api/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password, name: 'Dummy Name', email: 'dummy@example.com' }), // Add dummy name and email
            });

            const data = await response.json();

            if (response.ok) {
                displayMessage(registerMessage, 'Registration successful!', true);
                registerForm.reset(); // Reset the form on success
            } else {
                displayMessage(registerMessage, data.message || 'Registration failed.', false);
            }
        } catch (error) {
            displayMessage(registerMessage, 'An error occurred during registration.', false);
            console.error('Registration error:', error);
        }
    });

    // Login user
    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const username = document.getElementById('login-username').value;
        const password = document.getElementById('login-password').value;

        try {
            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password, name: 'Dummy Name', email: 'dummy@example.com' }), // Add dummy name and email
            });

            const data = await response.json();

            if (response.ok) {
                jwtToken = data.token;
                displayMessage(loginMessage, 'Login successful! Token obtained.', true);
                console.log('JWT Token:', jwtToken);
            } else {
                displayMessage(loginMessage, data.message || 'Login failed.', false);
                jwtToken = null;
            }
        } catch (error) {
            displayMessage(loginMessage, 'An error occurred during login.', false);
            console.error('Login error:', error);
            jwtToken = null;
        }
    });

    // Get all users (secured endpoint)
    getUsersButton.addEventListener('click', async () => {
        if (!jwtToken) {
            displayMessage(apiMessage, 'Please login first.', false);
            return;
        }

        try {
            const response = await fetch('/api/users', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json',
                },
            });

            const data = await response.json();

            if (response.ok) {
                displayMessage(apiMessage, 'Successfully fetched users.', true);
                usersList.innerHTML = ''; // Clear previous list
                if (data.length > 0) {
                    data.forEach(user => {
                        const p = document.createElement('p');
                        p.textContent = `ID: ${user.id}, Username: ${user.username}`;
                        usersList.appendChild(p);
                    });
                } else {
                    usersList.textContent = 'No users found.';
                }
            } else {
                displayMessage(apiMessage, data.message || 'Failed to fetch users.', false);
                usersList.innerHTML = '';
            }
        } catch (error) {
            displayMessage(apiMessage, 'An error occurred while fetching users.', false);
            console.error('Get users error:', error);
            usersList.innerHTML = '';
        }
    });
});
