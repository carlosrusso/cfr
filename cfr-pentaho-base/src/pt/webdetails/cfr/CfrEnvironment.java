/*!
* Copyright 2002 - 2013 Webdetails, a Pentaho company.  All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.cfr;

import pt.webdetails.cfr.bean.CoreBeanFactory;
import pt.webdetails.cfr.bean.ICfrBeanFactory;
import pt.webdetails.cfr.file.FileStorer;
import pt.webdetails.cfr.repository.IFileRepository;
import pt.webdetails.cpf.PentahoPluginEnvironment;

import pt.webdetails.cpf.persistence.PersistenceEngine;
import pt.webdetails.cpf.session.ISessionUtils;
import pt.webdetails.cpf.utils.IPluginUtils;

public class CfrEnvironment extends PentahoPluginEnvironment implements ICfrEnvironment {

  private static final String PLUGIN_NAME = "cfr";
  private static boolean persistenceEngineInitialized = false;

  private ICfrBeanFactory factory;
  private IFileRepository repository;


  public void init( ICfrBeanFactory factory ) {
    this.factory = factory;

    if ( factory.containsBean( IFileRepository.class.getSimpleName() ) ) {
      repository = (IFileRepository) factory.getBean( IFileRepository.class.getSimpleName() );
    }

    super.init( this );
  }

  public CfrEnvironment() {
    init( new CoreBeanFactory() );
  }

  @Override public IPluginUtils getPluginUtils() {
    return null;
  }

  @Override public String getPluginName() {
    return PLUGIN_NAME;
  }

  @Override public ISessionUtils getSessionUtils() {
    return null;
  }

  @Override public void reload() {

  }

  public IFileRepository getRepository() {
    return repository;
  }

  public static PersistenceEngine getPersistenceEngine() {

    PersistenceEngine engine = PersistenceEngine.getInstance();
    if ( !persistenceEngineInitialized ) {
      synchronized ( CfrEnvironment.class ) {
        if ( !persistenceEngineInitialized ) {
          if ( !engine.classExists( FileStorer.FILE_METADATA_STORE_CLASS ) ) {
            engine.initializeClass( FileStorer.FILE_METADATA_STORE_CLASS );
          }
          if ( !engine.classExists( FileStorer.FILE_PERMISSIONS_METADATA_STORE_CLASS ) ) {
            engine.initializeClass( FileStorer.FILE_PERMISSIONS_METADATA_STORE_CLASS );
          }
          persistenceEngineInitialized = true;
        }

      }
    }

    return engine;
  }

}
