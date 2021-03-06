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
const principalAuthoritiesElement = document.getElementById("principal-authority");

const mainDomain = "https://www.kubahruby.com"
//const mainDomain = "http://localhost:9091"

displaySuggestions();

function filterIssues() {
    let type = typeFilter.options[typeFilter.selectedIndex];
    let severity = severityFilter.options[severityFilter.selectedIndex];
    let status = statusFilter.options[statusFilter.selectedIndex];
    let project = projectFilter.options[projectFilter.selectedIndex];

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
            if (principalAuthoritiesElement.value.includes('ROLE_ADMIN')) {
                resultsToHtml += `
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
            }
        else {
                resultsToHtml += `
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
                                <a class="btn btn-primary issue-details-button btn-danger" onclick="deleteIssueNotAdmin()">
                                    Delete
                                </a>
                                <a class="btn btn-primary issue-details-button issue-card-button-details" onclick="displayIssueDetails(${issue.id})">
                                    Details
                                </a>
                            </div>
                        </div>
                    </div>
                `;
            }});
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
            if (principalAuthoritiesElement.value.includes('ROLE_ADMIN')) {
                resultsToHtml += `
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
            }
            else {
                resultsToHtml += `
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
                                    <a class="btn btn-primary issue-details-button btn-danger" onclick="deleteIssueNotAdmin()">
                                        Delete
                                    </a>
                                    <a class="btn btn-primary issue-details-button issue-card-button-details" onclick="displayIssueDetails(${issue.id})">
                                        Details
                                    </a>
                                </div>
                            </div>
                        </div>
                `;
            }
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
                    if (principalAuthoritiesElement.value.includes('ROLE_ADMIN')) {

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
                }
                else {
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
                                    <a class="btn btn-primary issue-details-button btn-danger" onclick="deleteIssueNotAdmin()">
                                        Delete
                                    </a>
                                    <a class="btn btn-primary issue-details-button issue-card-button-details" onclick="displayIssueDetails(${issue.id})">
                                        Details
                                    </a>
                                </div>
                            </div>
                        </div>
                `;
                    }});
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

function formatDateForDetails(sqlDate) {
    let date = sqlDate.split(/[- :T.]/);
    let jsDate = new Date(Date.UTC(date[0], date[1] - 1, date[2], date[3], date[4], date[5]));
    return moment(jsDate).format('DD.MM.YYYY HH:mm');
}

function displayIssueDetails(id) {
    let resultsToHtml = ``;

    function loadIssueDetailsHistory(issue) {
        let historyRecords = issue.historyRecords;
        let historyHtml = ``;
        historyRecords.slice().reverse().forEach(
            record => {
                let formattedDate = formatDateForDetails(record.date);
                historyHtml +=`
                <p style="margin-bottom: 0; font-size: 85%">
                    <span><span style="color: yellowgreen">${formattedDate}</span>: ${record.text}</span>
                </p>
                `;
            }
        );
        return historyHtml;
    }

    function loadIssueDetailsAttachments(issue) {
        let attachments = issue.attachments;
        let attachmentsHtml = ``;
        attachments.forEach(
            attachment => {
                if (principalAuthoritiesElement.value.includes('ROLE_ADMIN')) {
                    attachmentsHtml +=`
                    <p style="margin-bottom: 0; font-size: 85%">
                        <a href="${attachment.path}" download>
                            <span>${attachment.originalFilename} </span>
                        </a>
                        <a href="#" style="color: crimson" onclick="deleteAttachment(${attachment.id}, ${issue.id}, '${attachment.originalFilename}')">
                            <span> (delete)</span>
                        </a>
                    </p>
                    `;
                } else {
                    attachmentsHtml +=`
                    <p style="margin-bottom: 0; font-size: 85%">
                        <a href="${attachment.path}" download>
                            <span>${attachment.originalFilename} </span>
                        </a>
                    </p>
                    `;
                }
            }
        );
        return attachmentsHtml;
    }

    fetch(`${mainDomain}/issue/id/${id}`, {
        method: 'GET',
    })
        .then(response => response.json())
        .then(issue => {
            let formattedDate = formatDateForDetails(issue.date);
            if (principalAuthoritiesElement.value.includes('ROLE_ADMIN')) {
                resultsToHtml +=
                    `
                    <input type="text" id="displayed-issue-id" style="display: none" value='${JSON.stringify(issue).replace(/[\/\(\)\']/g, "&apos;")}'>
                    <h5>#<span id="issue-id">${issue.id}</span>  <span id="issue-title">${issue.title}</span></h5>
                    <p style="margin-bottom: 0">Author: ${issue.author}</p>
                    <p>Created: ${formattedDate}</p>
                    <p style="margin-bottom: 0">Type: <span style="font-weight: bold">${issue.type}</span></p>
                    <p style="margin-bottom: 0">Severity: <span style="font-weight: bold">${issue.severity}</span></p>
                    <p style="margin-bottom: 0">Status: <span style="font-weight: bold">${issue.status}</span></p>
                    <p >Project: <span style="font-weight: bold">${issue.project}</span></p>
                    <hr style="margin: 0.5rem -1rem 0.5rem;color: white">
                    <p>${issue.description}</p>
                    <div style="display: flex; justify-content: center">
                        <a id="update-description-button" href="/tracker/update">
                            <button class="btn btn-primary issue-details-button issue-card-button-details">Update description</button>
                        </a>
                    </div>
                    <hr style="margin: 0.5rem -1rem 0.5rem;color: white">
                    <p style="margin-bottom: 0">Attached files:</p>
                    <div id="issue-details-attachments"></div>
                    <div style="display: flex; justify-content: center">
                        <a id="add-file-button" href="/tracker/upload">
                            <button class="btn btn-primary issue-details-button issue-card-button-details">Add file</button>
                        </a>
                    </div>
                    <hr style="margin: 0.5rem -1rem 0.5rem;color: white">
                    <p style="margin-bottom: 0">History of changes:</p>
                    <div id="issue-details-history"></div>
                    `;
                detailsElement.innerHTML = resultsToHtml;
                let issueDetailsHistoryElement = document.getElementById("issue-details-history");
                issueDetailsHistoryElement.innerHTML = loadIssueDetailsHistory(issue);
                let issueDetailsAttachmentsElement = document.getElementById("issue-details-attachments");
                issueDetailsAttachmentsElement.innerHTML = loadIssueDetailsAttachments(issue);
                let updateButton = document.getElementById("update-description-button");
                updateButton.setAttribute("href", "/tracker/update?title=" + issue['title'] + "&description=" + issue['description']);
                let uploadButton = document.getElementById("add-file-button");
                uploadButton.setAttribute("href", "/tracker/upload?title=" + issue['title']);
                setSaveFormToIssueProperties(issue);

            } else {
                resultsToHtml +=
                    `
                    <input type="text" id="displayed-issue-id" style="display: none" value='${JSON.stringify(issue).replace(/[\/\(\)\']/g, "&apos;")}'>
                    <h5>#<span id="issue-id">${issue.id}</span>  <span id="issue-title">${issue.title}</span></h5>
                    <p style="margin-bottom: 0">Author: ${issue.author}</p>
                    <p>Created: ${formattedDate}</p>
                    <p style="margin-bottom: 0">Type: <span style="font-weight: bold">${issue.type}</span></p>
                    <p style="margin-bottom: 0">Severity: <span style="font-weight: bold">${issue.severity}</span></p>
                    <p style="margin-bottom: 0">Status: <span style="font-weight: bold">${issue.status}</span></p>
                    <p >Project: <span style="font-weight: bold">${issue.project}</span></p>
                    <hr style="margin: 0.5rem -1rem 0.5rem;color: white">
                    <p>${issue.description}</p>
                    
                    <div style="display: flex; justify-content: center">
                        <button type="submit" disabled class="btn btn-primary issue-details-button issue-card-button-details">Update description</button>
                    </div>
                    <hr style="margin: 0.5rem -1rem 0.5rem;color: white">
                    <p style="margin-bottom: 0">Attached files:</p>
                    <div id="issue-details-attachments"></div>
                    <div style="display: flex; justify-content: center">
                        <a id="add-file-button" href="/tracker/upload">
                            <button class="btn btn-primary issue-details-button issue-card-button-details">Add file</button>
                        </a>
                    </div>
                    <hr style="margin: 0.5rem -1rem 0.5rem;color: white">
                    <p style="margin-bottom: 0">History of changes:</p>
                    <div id="issue-details-history"></div>
                    `;
                detailsElement.innerHTML = resultsToHtml;
                let issueDetailsHistoryElement = document.getElementById("issue-details-history");
                issueDetailsHistoryElement.innerHTML = loadIssueDetailsHistory(issue);
                let issueDetailsAttachmentsElement = document.getElementById("issue-details-attachments");
                issueDetailsAttachmentsElement.innerHTML = loadIssueDetailsAttachments(issue);
                let uploadButton = document.getElementById("add-file-button");
                uploadButton.setAttribute("href", "/tracker/upload?title=" + issue['title']);
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
                        window.alert("There was an error deleting the report.");
                    } else {
                        window.alert(`Report #${id} ${title} was deleted successfully`);

                    }
                });
    }
    setTimeout(() => {searchIssues()}, 1000);
    //searchIssues();
    resetFilters();
}

function deleteIssueNotAdmin () {
    confirm(`Admin rights are required for this operation.`);
    }

function deleteAttachment (attachmentId, issueId, filename) {
    if (confirm(`delete file ${filename}?`)) {
        fetch(`${mainDomain}/issue/attachments/${attachmentId}`, {
            method: 'DELETE',
        })
            .then(
                function(response) {
                    if (response.status !== 204) {
                        window.alert("There was an error deleting the report.");
                    } else {
                        window.alert(`File ${filename} was deleted successfully`);

                    }
                    displayIssueDetails(issueId)
                });
    }
    //setTimeout(() => {searchIssues()}, 1000);
}

function updateIssue() {
    let type = typeSaveSelect.options[typeSaveSelect.selectedIndex];
    let severity = severitySaveSelect.options[severitySaveSelect.selectedIndex];
    let status = statusSaveSelect.options[statusSaveSelect.selectedIndex];
    let project = projectSaveSelect.options[projectSaveSelect.selectedIndex];
    let issueIdElement = document.getElementById("issue-id");
    let issueTitleElement = document.getElementById("issue-title");

    let hiddenIssue = document.getElementById("displayed-issue-id");
    let issueDto = {};

    issueDto.id = issueIdElement.innerHTML;
    issueDto.title = issueTitleElement.innerHTML;
    issueDto.type = type.value;
    issueDto.severity = severity.value;
    issueDto.status = status.value;
    issueDto.project = project.value;

    console.log(issueDto);

    let postBody = JSON.stringify(issueDto);
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
                window.alert("There was an error updating the report.");
            } else {
                window.alert(`Report #${issueDto.id} ${issueDto.title} was updated successfully`);
            }
        });

    let element = document.getElementById("status");
    element.value = issueDto['status'];
    setTimeout(() => {filterIssues()}, 1000);
    setTimeout(() => {displayIssueDetails(issueDto['id'])}, 1000);
}

