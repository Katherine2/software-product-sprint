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

function getWelcomeMessage() {
  fetch('/data').then(response => response.json()).then((messages) => {
    // stats is an object, not a string, so we have to
    // reference its fields to create HTML content

    const messagesListElement = document.getElementById('welcome-message-container');
    messagesListElement.innerHTML = '';
    messagesListElement.appendChild(
        createListElement('English: ' + messages.english));
    messagesListElement.appendChild(
        createListElement('French: ' + messages.french));
    messagesListElement.appendChild(
        createListElement('Spanish: ' + messages.spanish));
  });
}

function getWelcomeMessageEnglish() {
  fetch('/data').then(response => response.json()).then((messages) => {

    const messagesElement = document.getElementById('welcome-message-container-english');
    messagesElement.innerHTML = '';
    messagesElement.appendChild(
        createElement(messages.english));
  });
}

function getWelcomeMessageFrench() {
  fetch('/data').then(response => response.json()).then((messages) => {

    const messagesElement = document.getElementById('welcome-message-container-french');
    messagesElement.innerHTML = '';
    messagesElement.appendChild(
        createElement(messages.french));
  });
}

function getWelcomeMessageSpanish() {
  fetch('/data').then(response => response.json()).then((messages) => {

    const messagesElement = document.getElementById('welcome-message-container-spanish');
    messagesElement.innerHTML = '';
    messagesElement.appendChild(
        createElement(messages.spanish));
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function createElement(text) {
  const pElement = document.createElement('p');
  pElement.innerText = text;
  return pElement;
}