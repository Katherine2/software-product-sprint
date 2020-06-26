// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['You miss 100% of the shots you don\'t take', 'Sometimes it\'s the very people no one imagines anything of that do the things no one can imagine', 'You have to be odd to be number one', 'Why fit in when you were born to stand out'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Fetches a welcome message from the server and adds it to the DOM.
 */
function getComments(selectObject) {
  var lang = selectObject.value;

  var english = "en";
  var french = "fr";
  var spanish = "es"
  
  // The fetch() function returns a Promise because the request is asynchronous.
  // When the request is complete, pass the response into handleResponse().
  if(lang == english){
    const responsePromise = fetch('/data?lang=en');
    responsePromise.then(handleResponse);
  }
  else if(lang == french){
    const responsePromise = fetch('/data?lang=fr');
    responsePromise.then(handleResponse);
  }
  else if(lang == spanish){
    const responsePromise = fetch('/data?lang=es');
    responsePromise.then(handleResponse);
  }
  else{
      const errorMessage = "Please select a language from the options to view the comments";
      displayErrorMessage(errorMessage);
  }
}

/**
 * Handles response by converting it to text and passing the result to
 * addMessageToDom().
 */
function handleResponse(response) {

  // response.text() returns a Promise, because the response is a stream of
  // content and not a simple variable.
  const textPromise = response.text();

  // When the response is converted to text, pass the result into the
  // addQuoteToDom() function.
  textPromise.then(addCommentToDom);
}

/** Adds a welcome message to the DOM. */
function addCommentToDom(comment) {

  const commentContainer = document.getElementById('comment-container');
  commentContainer.innerText = comment;
}

function displayErrorMessage(message) {

  const commentContainer = document.getElementById('comment-container');
  commentContainer.innerText = message;
}
