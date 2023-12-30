
async function remove(endpoint, bearerToken = null) {
    const requestOptions = {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${bearerToken}`,
        },
    };

    const response = await fetch(`${endpoint}`, requestOptions);
    const data = await response.json();
    return data;
}

async function get(endpoint, bearerToken = null) {
    const requestOptions = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${bearerToken}`,
        },
    };
    const response = await fetch(`${endpoint}`, requestOptions);
    return await response.json();
}

async function post(endpoint, body = null, bearerToken = null) {
    const requestOptions = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${bearerToken}`,
        },
        body: JSON.stringify(body),
    };

    try {
        const response = await fetch(`${endpoint}`, requestOptions);
        const status = response.status;
        const data = await response.json();
        return { status, data };
    } catch (error) {
        return { status: 200, data: {} };
    }
}

async function update(type, bodyData, bearerToken = null) {
    const requestOptions = {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${bearerToken}`,
        },
        body: JSON.stringify(bodyData),
    };

    const response = await fetch(endpoint + type, requestOptions);
    return await response.json();
}

export { get, post, update, remove };
