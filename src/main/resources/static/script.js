document.addEventListener('DOMContentLoaded', () => {
    // --- Auth and User Management ---
    const registerForm = document.getElementById('register-form');
    const loginForm = document.getElementById('login-form');
    const getUsersButton = document.getElementById('get-users-button');
    const registerMessage = document.getElementById('register-message');
    const loginMessage = document.getElementById('login-message');
    const apiMessage = document.getElementById('api-message');
    const usersList = document.getElementById('users-list');

    let jwtToken = null;
    let userId = null;

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
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password, name: 'Dummy Name', email: 'dummy@example.com' })
            });
            const data = await response.json();
            if (response.ok) {
                displayMessage(registerMessage, 'Registration successful!', true);
                registerForm.reset();
            } else {
                displayMessage(registerMessage, data.message || 'Registration failed.', false);
            }
        } catch (error) {
            displayMessage(registerMessage, 'An error occurred during registration.', false);
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
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            const data = await response.json();
            if (response.ok) {
                jwtToken = data.token;
                displayMessage(loginMessage, 'Login successful! Token obtained.', true);
                // Get userId from users endpoint
                const userResp = await fetch(`/api/users`, {
                    method: 'GET',
                    headers: { 'Authorization': `Bearer ${jwtToken}` }
                });
                const users = await userResp.json();
                const user = users.find(u => u.username === username);
                userId = user ? user.id : null;
            } else {
                displayMessage(loginMessage, data.message || 'Login failed.', false);
                jwtToken = null;
                userId = null;
            }
        } catch (error) {
            displayMessage(loginMessage, 'An error occurred during login.', false);
            jwtToken = null;
            userId = null;
        }
    });

    // Get all users
    getUsersButton.addEventListener('click', async () => {
        if (!jwtToken) {
            displayMessage(apiMessage, 'Please login first.', false);
            return;
        }
        try {
            const response = await fetch('/api/users', {
                method: 'GET',
                headers: { 'Authorization': `Bearer ${jwtToken}` }
            });
            const data = await response.json();
            if (response.ok) {
                displayMessage(apiMessage, 'Successfully fetched users.', true);
                usersList.innerHTML = '';
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
            usersList.innerHTML = '';
        }
    });

    // --- Account Info ---
    const getAccountButton = document.getElementById('get-account-info');
    const accountInfo = document.getElementById('account-info');
    const accountMessage = document.getElementById('account-message');
    if (getAccountButton) {
        getAccountButton.addEventListener('click', async () => {
            if (!jwtToken || !userId) {
                displayMessage(accountMessage, 'Please login first.', false);
                return;
            }
            try {
                const response = await fetch(`/api/account/${userId}/balance`, {
                    method: 'GET',
                    headers: { 'Authorization': `Bearer ${jwtToken}` }
                });
                const data = await response.json();
                if (response.ok) {
                    displayMessage(accountMessage, 'Account info fetched.', true);
                    accountInfo.textContent = `Balance: $${data}`;
                } else {
                    displayMessage(accountMessage, data.message || 'Failed to fetch account info.', false);
                    accountInfo.textContent = '';
                }
            } catch (error) {
                displayMessage(accountMessage, 'An error occurred while fetching account info.', false);
                accountInfo.textContent = '';
            }
        });
    }

    // --- Deposit ---
    const depositForm = document.getElementById('deposit-form');
    const depositMessage = document.getElementById('deposit-message');
    if (depositForm) {
        depositForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            if (!jwtToken || !userId) {
                displayMessage(depositMessage, 'Please login first.', false);
                return;
            }
            const amount = document.getElementById('deposit-amount').value;
            const description = document.getElementById('deposit-description').value;
            try {
                const response = await fetch(`/api/account/${userId}/deposit?amount=${amount}&description=${encodeURIComponent(description)}`, {
                    method: 'POST',
                    headers: { 'Authorization': `Bearer ${jwtToken}` }
                });
                const data = await response.json();
                if (response.ok) {
                    displayMessage(depositMessage, 'Deposit successful!', true);
                } else {
                    displayMessage(depositMessage, data.message || 'Deposit failed.', false);
                }
            } catch (error) {
                displayMessage(depositMessage, 'An error occurred during deposit.', false);
            }
        });
    }

    // --- Withdraw ---
    const withdrawForm = document.getElementById('withdraw-form');
    const withdrawMessage = document.getElementById('withdraw-message');
    if (withdrawForm) {
        withdrawForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            if (!jwtToken || !userId) {
                displayMessage(withdrawMessage, 'Please login first.', false);
                return;
            }
            const amount = document.getElementById('withdraw-amount').value;
            const description = document.getElementById('withdraw-description').value;
            try {
                const response = await fetch(`/api/account/${userId}/withdraw?amount=${amount}&description=${encodeURIComponent(description)}`, {
                    method: 'POST',
                    headers: { 'Authorization': `Bearer ${jwtToken}` }
                });
                const data = await response.json();
                if (response.ok) {
                    displayMessage(withdrawMessage, 'Withdrawal successful!', true);
                } else {
                    displayMessage(withdrawMessage, data.message || 'Withdrawal failed.', false);
                }
            } catch (error) {
                displayMessage(withdrawMessage, 'An error occurred during withdrawal.', false);
            }
        });
    }

    // --- Transaction History ---
    const transactionHistoryButton = document.getElementById('get-transactions');
    const transactionHistoryDiv = document.getElementById('transaction-history');
    if (transactionHistoryButton) {
        transactionHistoryButton.addEventListener('click', async () => {
            if (!jwtToken || !userId) {
                displayMessage(transactionHistoryDiv, 'Please login first.', false);
                return;
            }
            try {
                const response = await fetch(`/api/transactions/${userId}`, {
                    method: 'GET',
                    headers: { 'Authorization': `Bearer ${jwtToken}` }
                });
                const data = await response.json();
                if (response.ok) {
                    displayMessage(transactionHistoryDiv, 'Transaction history fetched.', true);
                    transactionHistoryDiv.innerHTML = '';
                    if (data.length > 0) {
                        data.forEach(transaction => {
                            const p = document.createElement('p');
                            p.textContent = `ID: ${transaction.id}, Amount: $${transaction.amount}, Type: ${transaction.type}, Date: ${new Date(transaction.date).toLocaleString()}`;
                            transactionHistoryDiv.appendChild(p);
                        });
                    } else {
                        transactionHistoryDiv.textContent = 'No transactions found.';
                    }
                } else {
                    displayMessage(transactionHistoryDiv, data.message || 'Failed to fetch transaction history.', false);
                    transactionHistoryDiv.innerHTML = '';
                }
            } catch (error) {
                displayMessage(transactionHistoryDiv, 'An error occurred while fetching transaction history.', false);
                transactionHistoryDiv.innerHTML = '';
            }
        });
    }

    // --- Create Post ---
    const createPostForm = document.getElementById('create-post-form');
    const postMessage = document.getElementById('post-message');
    if (createPostForm) {
        createPostForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            if (!jwtToken || !userId) {
                displayMessage(postMessage, 'Please login first.', false);
                return;
            }
            const title = document.getElementById('post-title').value;
            const content = document.getElementById('post-content').value;
            try {
                const response = await fetch(`/api/posts/create?userId=${userId}&title=${encodeURIComponent(title)}&content=${encodeURIComponent(content)}`, {
                    method: 'POST',
                    headers: { 'Authorization': `Bearer ${jwtToken}` }
                });
                const data = await response.json();
                if (response.ok) {
                    displayMessage(postMessage, 'Post created successfully!', true);
                    createPostForm.reset();
                } else {
                    displayMessage(postMessage, data.message || 'Post creation failed.', false);
                }
            } catch (error) {
                displayMessage(postMessage, 'An error occurred while creating the post.', false);
            }
        });
    }
});
