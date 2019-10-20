/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.streampipes.model.client.pipeline;

import org.streampipes.model.SpDataStream;
import org.streampipes.model.graph.DataProcessorInvocation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;


public abstract class ElementComposition {

  @OneToMany(cascade = CascadeType.ALL)
  protected List<DataProcessorInvocation> sepas;

  @OneToMany(cascade = CascadeType.ALL)
  protected List<SpDataStream> streams;

  protected String name;
  protected String description;

  public ElementComposition() {
    this.sepas = new ArrayList<>();
    this.streams = new ArrayList<>();
  }

  public List<DataProcessorInvocation> getSepas() {
    return sepas;
  }

  public void setSepas(List<DataProcessorInvocation> sepas) {
    this.sepas = sepas;
  }

  public List<SpDataStream> getStreams() {
    return streams;
  }

  public void setStreams(List<SpDataStream> streams) {
    this.streams = streams;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
