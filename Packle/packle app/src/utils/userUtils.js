export const registerAccount = (user) => {
    return fetch("http://localhost:3001/user/register", {
            method: "POST",
            body: JSON.stringify(user),
            headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "no-cors",
            },
        });
}