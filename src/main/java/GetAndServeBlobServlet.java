/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static com.google.common.truth.Truth.assertThat;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that gets the blob information and serves the blob in the blob upload callback request.
 * The BlobKey of the uploaded blob is stored as the value of the response header BLOBKEY_HEADER.
 */
@WebServlet(name = "GetAndServeBlobServlet", value = "/prober/blob")
public class GetAndServeBlobServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(GetAndServeBlobServlet.class.getName());
  private static final String UPLOAD_NAME = "java-blobstore-test";
  private static final String BLOBKEY_HEADER = "Test-Blobkey";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    // Must be called from a blob upload callback request.
    assertThat(blobstoreService.getFileInfos(request).get(UPLOAD_NAME)).isNotEmpty();

    // Must be called from a blob upload callback request.
    assertThat(blobstoreService.getBlobInfos(request).get(UPLOAD_NAME)).isNotEmpty();

    // Must be called from a blob upload callback request.
    List<BlobKey> blobKeys = blobstoreService.getUploads(request).get(UPLOAD_NAME);
    assertThat(blobKeys).hasSize(1);
    response.setHeader(BLOBKEY_HEADER, blobKeys.get(0).getKeyString());
    logger.info(
        "GetAndServeBlobServlet setting blob key header: " + blobKeys.get(0).getKeyString());

    // The response should be assumed to be committed after invoking `serve()`, but custom headers
    // can be appended. If no exception is thrown, we can think `serve()` succeeds.
    blobstoreService.serve(blobKeys.get(0), response);
  }
}