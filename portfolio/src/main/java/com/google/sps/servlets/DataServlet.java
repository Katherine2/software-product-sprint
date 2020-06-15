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

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    //ArrayList<String> comments = new ArrayList<String>();
    //ArrayList<String> emails = new ArrayList<String>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //response.setContentType("text/html;");
    //response.getWriter().println("Hello and welcome to my portfolio!");

    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String email = (String) entity.getProperty("email");
      String text = (String) entity.getProperty("comment");
      long timestamp = (long) entity.getProperty("timestamp");

      Comment comment = new Comment(id, email, text, timestamp);
      comments.add(comment);
    }

     Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String email = getParameter(request, "email", "");
    String comment = getParameter(request, "comment", "");
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("email", email);
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    //emails.add(email);
    //comments.add(comment);
    
    //System.out.println(emails);     //confirming that it works and the emails entered are properly added to the arraylist
    //System.out.println(comments);   //confirming that it works and the comments entered are properly added to the arraylist
    
    response.sendRedirect("/commentSubmitted.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
