/*******************************************************************************
 * Copyright 2012 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.turbomanage.storm.api;

import com.turbomanage.storm.TableHelper;

/**
 * Interface which must be implemented by generated
 * database factory class.
 *
 * @author David M. Chandler
 */
public interface DatabaseFactory {

	/**
	 * @return database name
	 */
	String getName();

	/**
	 * @return database version
	 */
	int getVersion();

	/**
	 * @return all TableHelper classes associated with this database
	 */
	TableHelper[] getTableHelpers();

}
