const typeFilter = document.getElementById("type");
const severityFilter = document.getElementById("severity");
const statusFilter = document.getElementById("status");
const projectFilter = document.getElementById("project");
const resultsElement = document.getElementById("filter-results");
const typeSaveSelect = document.getElementById("type-save");
const severitySaveSelect = document.getElementById("severity-save");
const statusSaveSelect = document.getElementById("status-save");
const projectSaveSelect = document.getElementById("project-save");
const detailsElement = document.getElementById("issue-details-box");
const searchBar = document.getElementById("search-bar");
const principalAuthoritiesElement = document.getElementById("principal-authority")

//const mainDomain = "https://www.kubahruby.com"
const mainDomain = "http://localhost:9091"

displaySuggestions();

function filterIssues() {
    let type = typeFilter.options[typeFilter.selectedIndex]
    let severity = severityFilter.options[severityFilter.selectedIndex]
    let status = statusFilter.options[statusFilter.selectedIndex]
    let project = projectFilter.options[projectFilter.selectedIndex]

    let resultsToHtml = ``;
    let postData = {
    };

    if (type.value !== 'all') {
        postData.type = type.value
    }
    if (severity.value !== 'all') {
        postData.severity = severity.value
    }
    if (status.value !== 'all') {
        postData.status = status.value
    }
    if (project.value !== 'all') {
        postData.project = project.value
    }

    const postBody = JSON.stringify(postData);

    fetch(`${mainDomain}/issue/filter`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: postBody
    })
    .then(response => response.json())
    .then(results => {
        results.forEach(issue => {
            resultsToHtml +=`
                    <div class="card issue-card-custom">
                        <div class="issue-card-header-custom">
                            <h5 class="card-header" style="float:left">${issue.title}</h5>
                            <h5 class="card-header" style="float:right" >#${issue.id}</h5>
                        </div>
                        <div class="card-body issue-card-body">
                            <p class="card-text issue-description-text">${issue.description}</p>
                            <hr class="issue-card-hr-separator">
                            <div style="display: inline">
                                <div class="issue-card-properties" style="margin-right: 1rem">
                                    <p>Type: <span>${issue.type}</span></p>
                                    <p>Severity: <span>${issue.severity}</span></p>
                                </div>
                                <div class="issue-card-properties">
                                    <p>Status: <span>${issue.status}</span></p>
                                    <p>Project: <span>${issue.project}</span></p>
                                </div>
                                <a class="btn btn-primary issue-details-button btn-danger" onclick="deleteIssue(${issue.id}, '${issue.title}')">
                                    Delete
                                </a>
                                <a class="btn btn-primary issue-details-button issue-card-button-details" onclick="displayIssueDetails(${issue.id})">
                                    Details
                                </a>
                            </div>
                        </div>
                    </div>
                `;
        });
        resultsElement.innerHTML = resultsToHtml;
    });
}

function searchIssues() {

    let postBody = searchBar.value;
    let resultsToHtml = ``;

    fetch(`${mainDomain}/issue/search?query=${postBody}`, {
        method: 'GET'
    })
    .then(response => response.json())
    .then(results => {
        results.forEach(issue => {
            resultsToHtml +=`
                        <div class="card issue-card-custom">
                            <div class="issue-card-header-custom">
                                <h5 class="card-header" style="float:left">${issue.title}</h5>
                                <h5 class="card-header" style="float:right" >#${issue.id}</h5>
                            </div>
                            <div class="card-body issue-card-body">
                                <p class="card-text issue-description-text">${issue.description}</p>
                                <hr class="issue-card-hr-separator">
                                <div style="display: inline">
                                    <div class="issue-card-properties" style="margin-right: 1rem">
                                        <p>Type: <span>${issue.type}</span></p>
                                        <p>Severity: <span>${issue.severity}</span></p>
                                    </div>
                                    <div class="issue-card-properties">
                                        <p>Status: <span>${issue.status}</span></p>
                                        <p>Project: <span>${issue.project}</span></p>
                                    </div>
                                    <a class="btn btn-primary issue-details-button btn-danger" onclick="deleteIssue(${issue.id}, '${issue.title}')">
                                        Delete
                                    </a>
                                    <a class="btn btn-primary issue-details-button issue-card-button-details" onclick="displayIssueDetails(${issue.id})">
                                        Details
                                    </a>
                                </div>
                            </div>
                        </div>
                `;
        });
        resultsElement.innerHTML = resultsToHtml;
        resetFilters();
    });
}

function displaySuggestions() {
    searchBar.addEventListener('keyup', () => {
        let postBody = searchBar.value;
        let resultsToHtml = ``;

        fetch(`${mainDomain}/issue/search?query=${postBody}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(results => {
                results.forEach(issue => {
                    resultsToHtml +=`
                        <div class="card issue-card-custom">
                            <div class="issue-card-header-custom">
                                <h5 class="card-header" style="float:left">${issue.title}</h5>
                                <h5 class="card-header" style="float:right" >#${issue.id}</h5>
                            </div>
                            <div class="card-body issue-card-body">
                                <p class="card-text issue-description-text">${issue.description}</p>
                                <hr class="issue-card-hr-separator">
                                <div style="display: inline">
                                    <div class="issue-card-properties" style="margin-right: 1rem">
                                        <p>Type: <span>${issue.type}</span></p>
                                        <p>Severity: <span>${issue.severity}</span></p>
                                    </div>
                                    <div class="issue-card-properties">
                                        <p>Status: <span>${issue.status}</span></p>
                                        <p>Project: <span>${issue.project}</span></p>
                                    </div>
                                    <a class="btn btn-primary issue-details-button btn-danger" onclick="deleteIssue(${issue.id}, '${issue.title}')">
                                        Delete
                                    </a>
                                    <a class="btn btn-primary issue-details-button issue-card-button-details" onclick="displayIssueDetails(${issue.id})">
                                        Details
                                    </a>
                                </div>
                            </div>
                        </div>
                `;
                });
                resultsElement.innerHTML = resultsToHtml;
                resetFilters();
            });
    })
}

function resetFilters() {
    setSelectElement("type", "all");
    setSelectElement("severity", "all");
    setSelectElement("status", "all");
    setSelectElement("project", "all");
}

function setSaveFormToIssueProperties(issue) {
    setSelectElement("type-save", issue.type);
    setSelectElement("severity-save", issue.severity);
    setSelectElement("status-save", issue.status);
    setSelectElement("project-save", issue.project);
}

function displayIssueDetails(id) {
    let resultsToHtml = ``;
    fetch(`${mainDomain}/issue/id/${id}`, {
        method: 'GET',
    })
        .then(response => response.json())
        .then(issue => {
            if (principalAuthoritiesElement.value.includes('ROLE_ADMIN')) {
                resultsToHtml +=
                    `
                    <input type="text" id="displayed-issue-id" style="display: none" value='${JSON.stringify(issue).replace(/[\/\(\)\']/g, "&apos;")}'>
                    <h5>${issue.title}</h5>
                    <p style="margin-bottom: 0">Author: ${issue.author}</p>
                    <p>Created: ${issue.date}</p>
                    <p style="margin-bottom: 0">Type: <span style="font-weight: bold">${issue.type}</span></p>
                    <p style="margin-bottom: 0">Severity: <span style="font-weight: bold">${issue.severity}</span></p>
                    <p style="margin-bottom: 0">Status: <span style="font-weight: bold">${issue.status}</span></p>
                    <p >Project: <span style="font-weight: bold">${issue.project}</span></p>
                    <hr style="margin: 0.5rem -1rem 0.5rem;color: white">
                    <p>${issue.description}</p>
                    <div style="display: flex; justify-content: center">
                        <a id="update-description-button" href="/tracker/update"><button class="btn btn-primary btn-lg btn-block submit-button" style="max-height: 3rem">Update description</button></a>
                    </div>
                    `;
                detailsElement.innerHTML = resultsToHtml;
                let updateButton = document.getElementById("update-description-button")
                updateButton.setAttribute("href", "/tracker/update?title=" + issue['title'] + "&description=" + issue['description'])
                setSaveFormToIssueProperties(issue);

            } else {
                resultsToHtml +=
                    `
                    <input type="text" id="displayed-issue-id" style="display: none" value='${JSON.stringify(issue).replace(/[\/\(\)\']/g, "&apos;")}'>
                    <h5>${issue.title}</h5>
                    <p style="margin-bottom: 0">Author: ${issue.author}</p>
                    <p>Created: ${issue.date}</p>
                    <p style="margin-bottom: 0">Type: <span style="font-weight: bold">${issue.type}</span></p>
                    <p style="margin-bottom: 0">Severity: <span style="font-weight: bold">${issue.severity}</span></p>
                    <p style="margin-bottom: 0">Status: <span style="font-weight: bold">${issue.status}</span></p>
                    <p >Project: <span style="font-weight: bold">${issue.project}</span></p>
                    <hr style="margin: 0.5rem -1rem 0.5rem;color: white">
                    <p>${issue.description}</p>
                    
                    <div style="display: flex; justify-content: center">
                        <button type="submit" disabled class="btn btn-primary btn-lg btn-block submit-button" style="max-height: 3rem">Update description</button>
                    </div>
                    `;
                detailsElement.innerHTML = resultsToHtml;
                let updateButton = document.getElementById("update-description-button")
                updateButton.setAttribute("href", "/tracker/update?title=" + issue['title'] + "&description=" + issue['description'])
                setSaveFormToIssueProperties(issue);
            }
        });
}

function setSelectElement(id, value) {
    let element = document.getElementById(id);
    element.value = value;
}

function deleteIssue (id, title) {
    if (confirm(`delete report #${id} ${title}?`)) {
        fetch(`${mainDomain}/issue/id/${id}`, {
            method: 'DELETE',
        })
            .then(
                function(response) {
                    if (response.status !== 200) {
                        window.alert("There was an error deleting the report.")
                    } else {
                        window.alert(`Report #${id} ${title} was deleted successfully`)

                    }
                });
    }
    setTimeout(() => {searchIssues()}, 1000);
    //searchIssues();
    resetFilters();
}


function updateIssue() {
    let type = typeSaveSelect.options[typeSaveSelect.selectedIndex]
    let severity = severitySaveSelect.options[severitySaveSelect.selectedIndex]
    let status = statusSaveSelect.options[statusSaveSelect.selectedIndex]
    let project = projectSaveSelect.options[projectSaveSelect.selectedIndex]

    let hiddenIssue = document.getElementById("displayed-issue-id");
    let issue = JSON.parse(hiddenIssue.value);

    issue.type = type.value;
    issue.severity = severity.value;
    issue.status = status.value;
    issue.project = project.value;

    console.log(issue);

    let postBody = JSON.stringify(issue);
    fetch(`${mainDomain}/issue/update`, {
        headers: {
            'Content-Type': 'application/json',
        },
        method: 'PUT',
        body: postBody
    })
    .then(
        function(response) {
            if (response.status !== 200) {
                window.alert("There was an error updating the report.")
            } else {
                window.alert(`Report #${issue.id} ${issue.title} was updated successfully`)
            }
        });

    let element = document.getElementById("status");
    element.value = issue['status'];
    setTimeout(() => {filterIssues()}, 1000);
    setTimeout(() => {displayIssueDetails(issue['id'])}, 1000);
}

