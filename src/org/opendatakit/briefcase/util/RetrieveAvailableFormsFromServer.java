/*
 * Copyright (C) 2011 University of Washington.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.briefcase.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.opendatakit.briefcase.model.FormStatus;
import org.opendatakit.briefcase.model.IFormDefinition;
import org.opendatakit.briefcase.model.ParsingException;
import org.opendatakit.briefcase.model.RemoteFormDefinition;
import org.opendatakit.briefcase.model.ServerConnectionInfo;
import org.opendatakit.briefcase.model.TerminationFuture;
import org.opendatakit.briefcase.model.XmlDocumentFetchException;
import org.opendatakit.briefcase.operations.RetrieveAvailableFormsException;

public class RetrieveAvailableFormsFromServer {
  final ServerConnectionInfo originServerInfo;
  final TerminationFuture terminationFuture;
  List<FormStatus> formStatuses = new ArrayList<FormStatus>();

  public RetrieveAvailableFormsFromServer(ServerConnectionInfo originServerInfo,
                                          TerminationFuture terminationFuture) {
    this.originServerInfo = originServerInfo;
    this.terminationFuture = terminationFuture;
  }

  public void doAction() throws XmlDocumentFetchException, ParsingException {
    List<RemoteFormDefinition> formDefs = Collections.emptyList();
    formDefs = ServerFetcher.retrieveAvailableFormsFromServer(originServerInfo, terminationFuture);
    for (IFormDefinition fd : formDefs) {
      formStatuses.add(new FormStatus(FormStatus.TransferType.GATHER, fd));
    }
  }

  public List<FormStatus> getAvailableForms() {
    return formStatuses;
  }

  public static List<FormStatus> get(ServerConnectionInfo transferSettings) {
    RetrieveAvailableFormsFromServer action = new RetrieveAvailableFormsFromServer(transferSettings, new TerminationFuture());
    try {
      action.doAction();
      return action.formStatuses;
    } catch (ParsingException | XmlDocumentFetchException e) {
      throw new RetrieveAvailableFormsException(e);
    }
  }
}
